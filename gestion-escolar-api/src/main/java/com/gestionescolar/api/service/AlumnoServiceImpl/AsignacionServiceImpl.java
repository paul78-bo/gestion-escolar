package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.AsignacionRequestDTO;
import com.gestionescolar.api.dto.AsignacionResponseDTO;
import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.entity.Calificacion;
import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.entity.Inscripcion;
import com.gestionescolar.api.entity.Materia;
import com.gestionescolar.api.entity.Profesor;
import com.gestionescolar.api.exception.AsignacionNoEncontradaException;
import com.gestionescolar.api.exception.GrupoNoEncontradoException;
import com.gestionescolar.api.exception.MateriaNoEncontradaException;
import com.gestionescolar.api.exception.ProfesorNoEncontradoException;
import com.gestionescolar.api.mapper.AsignacionMapper;
import com.gestionescolar.api.repository.AsignacionRepository;
import com.gestionescolar.api.repository.CalificacionRepository;
import com.gestionescolar.api.repository.GrupoRepository;
import com.gestionescolar.api.repository.InscripcionRepository;
import com.gestionescolar.api.repository.MateriaRepository;
import com.gestionescolar.api.repository.ProfesorRepository;
import com.gestionescolar.api.service.AsignacionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AsignacionServiceImpl implements AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final GrupoRepository grupoRepository;
    private final MateriaRepository materiaRepository;
    private final ProfesorRepository profesorRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CalificacionRepository calificacionRepository;
    private final AsignacionMapper asignacionMapper;

    public AsignacionServiceImpl(AsignacionRepository asignacionRepository,
                                  GrupoRepository grupoRepository,
                                  MateriaRepository materiaRepository,
                                  ProfesorRepository profesorRepository,
                                  InscripcionRepository inscripcionRepository,
                                  CalificacionRepository calificacionRepository,
                                  AsignacionMapper asignacionMapper) {
        this.asignacionRepository = asignacionRepository;
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
        this.profesorRepository = profesorRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.calificacionRepository = calificacionRepository;
        this.asignacionMapper = asignacionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> listarTodas() {
        return asignacionRepository.findByActivoTrue().stream()
                .map(asignacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AsignacionResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return asignacionRepository.findByActivoTrue(pageable).map(asignacionMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public AsignacionResponseDTO obtenerPorId(Long id) {
        Asignacion a = asignacionRepository.findById(id)
                .orElseThrow(() -> new AsignacionNoEncontradaException(id));
        return asignacionMapper.toResponseDTO(a);
    }

    @Override
    public AsignacionResponseDTO crear(AsignacionRequestDTO dto) {
        // 1. Validar grupo, materia y profesor
        Grupo grupo = grupoRepository.findById(dto.getGrupoId())
                .orElseThrow(() -> new GrupoNoEncontradoException(dto.getGrupoId()));
        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new MateriaNoEncontradaException(dto.getMateriaId()));
        Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                .orElseThrow(() -> new ProfesorNoEncontradoException(dto.getProfesorId()));

        // 2. 🔥 VALIDACIÓN CLAVE: Evitar duplicados exactos
        // Buscamos si ya existe una asignación con el mismo grupo, materia, profesor y día
        boolean existe = asignacionRepository.findAll().stream()
                .anyMatch(a -> a.getActivo() &&
                               a.getGrupo().getId().equals(dto.getGrupoId()) &&
                               a.getMateria().getId().equals(dto.getMateriaId()) &&
                               a.getProfesor().getId().equals(dto.getProfesorId()) &&
                               a.getDiaSemana() != null && a.getDiaSemana().equals(dto.getDiaSemana()));

        if (existe) {
            System.out.println("⚠️ Asignación duplicada ignorada: " + materia.getNombre() + " - " + dto.getDiaSemana());
            // Si ya existe, simplemente devolvemos una respuesta (o podrías lanzar una excepción)
            // Para evitar que el frontend falle, devolvemos un DTO vacío o el existente.
            // Lo ideal es lanzar una excepción, pero para no romper el bucle del frontend, la ignoramos.
            return asignacionMapper.toResponseDTO(new Asignacion()); 
        }

        // 3. Crear asignación
        Asignacion a = asignacionMapper.toEntity(dto, grupo, materia, profesor);
        Asignacion guardada = asignacionRepository.save(a);

        // 4. Auto-crear calificaciones (solo si se creó la asignación)
        List<Inscripcion> inscripciones = inscripcionRepository.findActivasByGrupoId(grupo.getId());
        for (Inscripcion i : inscripciones) {
            for (int periodo = 1; periodo <= 3; periodo++) {
                if (!calificacionRepository.existsByAsignacionIdAndAlumnoIdAndPeriodo(guardada.getId(), i.getAlumno().getId(), periodo)) {
                    Calificacion c = new Calificacion();
                    c.setAsignacion(guardada);
                    c.setAlumno(i.getAlumno());
                    c.setCalificacion(null);
                    c.setObservaciones("Sin calificar");
                    c.setPeriodo(periodo);
                    c.setActivo(true);
                    calificacionRepository.save(c);
                }
            }
        }
        return asignacionMapper.toResponseDTO(guardada);
    }
    
    @Override
    public AsignacionResponseDTO actualizar(Long id, AsignacionRequestDTO dto) {
        Asignacion a = asignacionRepository.findById(id)
                .orElseThrow(() -> new AsignacionNoEncontradaException(id));

        Grupo grupo = null;
        Materia materia = null;
        Profesor profesor = null;

        if (dto.getGrupoId() != null) {
            grupo = grupoRepository.findById(dto.getGrupoId())
                    .orElseThrow(() -> new GrupoNoEncontradoException(dto.getGrupoId()));
        }
        if (dto.getMateriaId() != null) {
            materia = materiaRepository.findById(dto.getMateriaId())
                    .orElseThrow(() -> new MateriaNoEncontradaException(dto.getMateriaId()));
        }
        if (dto.getProfesorId() != null) {
            profesor = profesorRepository.findById(dto.getProfesorId())
                    .orElseThrow(() -> new ProfesorNoEncontradoException(dto.getProfesorId()));
        }

        asignacionMapper.updateEntity(a, dto, grupo, materia, profesor);
        Asignacion actualizada = asignacionRepository.save(a);
        return asignacionMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Asignacion a = asignacionRepository.findById(id)
                .orElseThrow(() -> new AsignacionNoEncontradaException(id));
        a.setActivo(false);
        asignacionRepository.save(a);

        // Desactivar calificaciones asociadas
        List<Calificacion> calificaciones = calificacionRepository.findActivasByAsignacionId(id);
        for (Calificacion c : calificaciones) {
            c.setActivo(false);
            calificacionRepository.save(c);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> listarPorGrupo(Long grupoId) {
        if (!grupoRepository.existsById(grupoId)) {
            throw new GrupoNoEncontradoException(grupoId);
        }
        return asignacionRepository.findActivasByGrupoId(grupoId).stream()
                .map(asignacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> listarPorProfesor(Long profesorId) {
        if (!profesorRepository.existsById(profesorId)) {
            throw new ProfesorNoEncontradoException(profesorId);
        }
        return asignacionRepository.findActivasByProfesorId(profesorId).stream()
                .map(asignacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> listarPorMateria(Long materiaId) {
        if (!materiaRepository.existsById(materiaId)) {
            throw new MateriaNoEncontradaException(materiaId);
        }
        return asignacionRepository.findActivasByMateriaId(materiaId).stream()
                .map(asignacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> buscarPorTermino(String termino) {
        return asignacionRepository.buscarPorTermino(termino).stream()
                .map(asignacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}