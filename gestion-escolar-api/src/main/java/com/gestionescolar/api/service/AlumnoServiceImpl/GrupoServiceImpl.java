package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.GrupoRequestDTO;
import com.gestionescolar.api.dto.GrupoResponseDTO;
import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.entity.CicloEscolar;
import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.exception.CicloEscolarNoEncontradoException;
import com.gestionescolar.api.exception.GrupoNoEncontradoException;
import com.gestionescolar.api.exception.ProfesorNoEncontradoException;
import com.gestionescolar.api.exception.MateriaNoEncontradaException;
import com.gestionescolar.api.mapper.GrupoMapper;
import com.gestionescolar.api.repository.AsignacionRepository;
import com.gestionescolar.api.repository.CicloEscolarRepository;
import com.gestionescolar.api.repository.GrupoRepository;
import com.gestionescolar.api.repository.MateriaRepository;
import com.gestionescolar.api.repository.ProfesorRepository;
import com.gestionescolar.api.service.GrupoService;

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
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final CicloEscolarRepository cicloRepository;
    private final AsignacionRepository asignacionRepository;
    private final ProfesorRepository profesorRepository;
    private final MateriaRepository materiaRepository;
    private final GrupoMapper grupoMapper;

    public GrupoServiceImpl(GrupoRepository grupoRepository,
                            CicloEscolarRepository cicloRepository,
                            AsignacionRepository asignacionRepository,
                            ProfesorRepository profesorRepository,
                            MateriaRepository materiaRepository,
                            GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.cicloRepository = cicloRepository;
        this.asignacionRepository = asignacionRepository;
        this.profesorRepository = profesorRepository;
        this.materiaRepository = materiaRepository;
        this.grupoMapper = grupoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> listarTodos() {
        return grupoRepository.findAll().stream().map(grupoMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> listarActivos() {
        return grupoRepository.findByActivoTrue().stream().map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GrupoResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return grupoRepository.findByActivoTrue(pageable).map(grupoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponseDTO obtenerPorId(Long id) {
        Grupo g = grupoRepository.findById(id).orElseThrow(() -> new GrupoNoEncontradoException(id));
        return grupoMapper.toResponseDTO(g);
    }

    @Override
    public GrupoResponseDTO crear(GrupoRequestDTO dto) {
        // Validar que exista el ciclo
        CicloEscolar ciclo = cicloRepository.findById(dto.getCicloId())
                .orElseThrow(() -> new CicloEscolarNoEncontradoException(dto.getCicloId()));

        // Generar nombre automático
        String nombre = dto.getGrado() + dto.getLetra();

        // Validar unicidad
        if (grupoRepository.existsByNombreAndCicloId(nombre, dto.getCicloId())) {
            throw new RuntimeException("Ya existe el grupo " + nombre + " en el ciclo " + ciclo.getNombre());
        }

        Grupo g = grupoMapper.toEntity(dto, ciclo);
        Grupo guardado = grupoRepository.save(g);
        return grupoMapper.toResponseDTO(guardado);
    }

    @Override
    public GrupoResponseDTO actualizar(Long id, GrupoRequestDTO dto) {
        Grupo g = grupoRepository.findById(id).orElseThrow(() -> new GrupoNoEncontradoException(id));

        CicloEscolar ciclo = null;
        if (dto.getCicloId() != null) {
            ciclo = cicloRepository.findById(dto.getCicloId())
                    .orElseThrow(() -> new CicloEscolarNoEncontradoException(dto.getCicloId()));
        }

        // Validar unicidad si cambió el nombre
        String nombreNuevo = dto.getGrado() + dto.getLetra();
        Long cicloIdNuevo = dto.getCicloId() != null ? dto.getCicloId() : g.getCiclo().getId();
        if (!g.getNombre().equals(nombreNuevo) &&
                grupoRepository.existsByNombreAndCicloId(nombreNuevo, cicloIdNuevo)) {
            throw new RuntimeException("Ya existe el grupo " + nombreNuevo);
        }

        grupoMapper.updateEntity(g, dto, ciclo);
        Grupo actualizado = grupoRepository.save(g);
        return grupoMapper.toResponseDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Grupo g = grupoRepository.findById(id).orElseThrow(() -> new GrupoNoEncontradoException(id));
        g.setActivo(false);
        grupoRepository.save(g);
    }

    // ==========================================
    // ADAPTADOS AL NUEVO MODELO
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> listarPorProfesor(Long profesorId) {
        if (!profesorRepository.existsById(profesorId)) {
            throw new ProfesorNoEncontradoException(profesorId);
        }
        // Buscar asignaciones del profesor y obtener los grupos únicos
        List<Asignacion> asignaciones = asignacionRepository.findActivasByProfesorId(profesorId);
        return asignaciones.stream()
                .map(Asignacion::getGrupo)
                .distinct()
                .map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> listarPorMateria(Long materiaId) {
        if (!materiaRepository.existsById(materiaId)) {
            throw new MateriaNoEncontradaException(materiaId);
        }
        // Buscar asignaciones de la materia y obtener los grupos únicos
        List<Asignacion> asignaciones = asignacionRepository.findActivasByMateriaId(materiaId);
        return asignaciones.stream()
                .map(Asignacion::getGrupo)
                .distinct()
                .map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> buscarPorTermino(String termino) {
        return grupoRepository.buscarPorTermino(termino).stream()
                .map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}