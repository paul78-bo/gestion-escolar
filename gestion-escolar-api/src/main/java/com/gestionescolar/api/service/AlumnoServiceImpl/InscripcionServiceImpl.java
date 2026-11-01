package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.InscripcionRequestDTO;
import com.gestionescolar.api.dto.InscripcionResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.entity.Calificacion;
import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.entity.Inscripcion;
import com.gestionescolar.api.exception.AlumnoNoEncontradoException;
import com.gestionescolar.api.exception.GrupoNoEncontradoException;
import com.gestionescolar.api.exception.InscripcionNoEncontradaException;
import com.gestionescolar.api.mapper.InscripcionMapper;
import com.gestionescolar.api.repository.AlumnoRepository;
import com.gestionescolar.api.repository.AsignacionRepository;
import com.gestionescolar.api.repository.CalificacionRepository;
import com.gestionescolar.api.repository.GrupoRepository;
import com.gestionescolar.api.repository.InscripcionRepository;
import com.gestionescolar.api.service.InscripcionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;
    private final AsignacionRepository asignacionRepository;
    private final CalificacionRepository calificacionRepository;
    private final InscripcionMapper inscripcionMapper;

    public InscripcionServiceImpl(InscripcionRepository inscripcionRepository,
                                  AlumnoRepository alumnoRepository,
                                  GrupoRepository grupoRepository,
                                  AsignacionRepository asignacionRepository,
                                  CalificacionRepository calificacionRepository,
                                  InscripcionMapper inscripcionMapper) {
        this.inscripcionRepository = inscripcionRepository;
        this.alumnoRepository = alumnoRepository;
        this.grupoRepository = grupoRepository;
        this.asignacionRepository = asignacionRepository;
        this.calificacionRepository = calificacionRepository;
        this.inscripcionMapper = inscripcionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarTodas() {
        return inscripcionRepository.findByActivoTrue().stream()
                .map(inscripcionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscripcionResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return inscripcionRepository.findByActivoTrue(pageable).map(inscripcionMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponseDTO obtenerPorId(Long id) {
        Inscripcion i = inscripcionRepository.findById(id)
                .orElseThrow(() -> new InscripcionNoEncontradaException(id));
        return inscripcionMapper.toResponseDTO(i);
    }

    @Override
    public InscripcionResponseDTO crear(InscripcionRequestDTO dto) {
        // 1. Validar alumno
        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
                .orElseThrow(() -> new AlumnoNoEncontradoException(dto.getAlumnoId()));

        // 2. Validar grupo
        Grupo grupo = grupoRepository.findById(dto.getGrupoId())
                .orElseThrow(() -> new GrupoNoEncontradoException(dto.getGrupoId()));

        // 3. Validar que el grupo esté activo
        if (!grupo.getActivo()) {
            throw new RuntimeException("El grupo " + grupo.getNombre() + " no está activo");
        }

        // 4. Validar que el grupo tenga capacidad
        if (grupo.getInscritos() >= grupo.getCapacidad()) {
            throw new RuntimeException("El grupo " + grupo.getNombre() + " está lleno");
        }

        // 5. Validar que no exista duplicado
        if (inscripcionRepository.existsByAlumnoIdAndGrupoId(dto.getAlumnoId(), dto.getGrupoId())) {
            throw new RuntimeException("El alumno ya está inscrito en este grupo");
        }

        // 6. Crear inscripción
        Inscripcion i = inscripcionMapper.toEntity(dto, alumno, grupo);
        Inscripcion guardada = inscripcionRepository.save(i);

        // 7. Incrementar inscritos del grupo
        grupo.setInscritos(grupo.getInscritos() + 1);
        grupoRepository.save(grupo);

        // 8. Auto-crear calificaciones para todas las asignaciones del grupo
        List<Asignacion> asignaciones = asignacionRepository.findActivasByGrupoId(grupo.getId());
        for (Asignacion asignacion : asignaciones) {
            Calificacion c = new Calificacion();
            c.setAsignacion(asignacion);
            c.setAlumno(alumno);
            c.setCalificacion(null);  // Sin calificar aún
            c.setObservaciones("Sin calificar");
            c.setActivo(true);
            calificacionRepository.save(c);
        }

        return inscripcionMapper.toResponseDTO(guardada);
    }

    @Override
    public InscripcionResponseDTO actualizar(Long id, InscripcionRequestDTO dto) {
        Inscripcion i = inscripcionRepository.findById(id)
                .orElseThrow(() -> new InscripcionNoEncontradaException(id));

        inscripcionMapper.updateEntity(i, dto);
        Inscripcion actualizada = inscripcionRepository.save(i);
        return inscripcionMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion i = inscripcionRepository.findById(id)
                .orElseThrow(() -> new InscripcionNoEncontradaException(id));

        // 1. Decrementar inscritos del grupo
        if (i.getGrupo() != null) {
            Grupo grupo = i.getGrupo();
            if (grupo.getInscritos() > 0) {
                grupo.setInscritos(grupo.getInscritos() - 1);
                grupoRepository.save(grupo);
            }
        }

        // 2. Marcar inscripción como inactiva
        i.setActivo(false);
        inscripcionRepository.save(i);

        // 3. Marcar calificaciones asociadas como inactivas
        List<Calificacion> calificaciones = calificacionRepository.findActivasByAlumnoId(i.getAlumno().getId());
        for (Calificacion c : calificaciones) {
            if (c.getAsignacion().getGrupo().getId().equals(i.getGrupo().getId())) {
                c.setActivo(false);
                calificacionRepository.save(c);
            }
        }
    }

    // ==========================================
    // ADAPTADOS AL NUEVO MODELO
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarPorAlumno(Long alumnoId) {
        if (!alumnoRepository.existsById(alumnoId)) {
            throw new AlumnoNoEncontradoException(alumnoId);
        }
        return inscripcionRepository.findActivasByAlumnoId(alumnoId).stream()
                .map(inscripcionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarPorGrupo(Long grupoId) {
        if (!grupoRepository.existsById(grupoId)) {
            throw new GrupoNoEncontradoException(grupoId);
        }
        return inscripcionRepository.findActivasByGrupoId(grupoId).stream()
                .map(inscripcionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // MÉTODOS QUE YA NO APLICAN AL NUEVO MODELO
    // Se mantienen para no romper la interfaz, pero lanzan excepción
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarPorMateria(Long materiaId) {
        throw new UnsupportedOperationException(
                "El método 'listarPorMateria' ya no aplica. Usa '/api/asignaciones/materia/{materiaId}'");
    }

    @Override
    @Transactional(readOnly = true)
    public long contarAlumnosPorMateria(Long materiaId) {
        throw new UnsupportedOperationException(
                "El método 'contarAlumnosPorMateria' ya no aplica. Usa '/api/asignaciones/materia/{materiaId}'");
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioAlumno(Long alumnoId) {
        return calificacionRepository.calcularPromedioByAlumnoId(alumnoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> obtenerHistorial(Long alumnoId, Long materiaId) {
        throw new UnsupportedOperationException(
                "El método 'obtenerHistorial' ya no aplica. Usa '/api/calificaciones/alumno/{alumnoId}'");
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> buscarPorTermino(String termino) {
        return inscripcionRepository.buscarPorTermino(termino).stream()
                .map(inscripcionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}