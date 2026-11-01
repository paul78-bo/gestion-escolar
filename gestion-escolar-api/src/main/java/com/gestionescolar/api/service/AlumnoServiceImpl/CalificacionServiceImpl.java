package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.CalificacionRequestDTO;
import com.gestionescolar.api.dto.CalificacionResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.entity.Calificacion;
import com.gestionescolar.api.exception.AlumnoNoEncontradoException;
import com.gestionescolar.api.exception.AsignacionNoEncontradaException;
import com.gestionescolar.api.exception.CalificacionNoEncontradaException;
import com.gestionescolar.api.mapper.CalificacionMapper;
import com.gestionescolar.api.repository.AlumnoRepository;
import com.gestionescolar.api.repository.AsignacionRepository;
import com.gestionescolar.api.repository.CalificacionRepository;
import com.gestionescolar.api.service.CalificacionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final AlumnoRepository alumnoRepository;
    private final AsignacionRepository asignacionRepository;
    private final CalificacionMapper calificacionMapper;

    public CalificacionServiceImpl(CalificacionRepository calificacionRepository,
                                    AlumnoRepository alumnoRepository,
                                    AsignacionRepository asignacionRepository,
                                    CalificacionMapper calificacionMapper) {
        this.calificacionRepository = calificacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.asignacionRepository = asignacionRepository;
        this.calificacionMapper = calificacionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> listarTodas() {
        return calificacionRepository.findByActivoTrue().stream()
                .map(calificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalificacionResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return calificacionRepository.findByActivoTrue(pageable).map(calificacionMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CalificacionResponseDTO obtenerPorId(Long id) {
        Calificacion c = calificacionRepository.findById(id)
                .orElseThrow(() -> new CalificacionNoEncontradaException(id));
        return calificacionMapper.toResponseDTO(c);
    }

    @Override
    public CalificacionResponseDTO crear(CalificacionRequestDTO dto) {
        // 1. Validar asignación
        Asignacion asignacion = asignacionRepository.findById(dto.getAsignacionId())
                .orElseThrow(() -> new AsignacionNoEncontradaException(dto.getAsignacionId()));

        // 2. Validar alumno
        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
                .orElseThrow(() -> new AlumnoNoEncontradoException(dto.getAlumnoId()));
        
        if (dto.getPeriodo() == null || dto.getPeriodo() < 1 || dto.getPeriodo() > 3) {
            throw new RuntimeException("El periodo debe ser 1, 2 o 3");
        }
        
        // 3. 👈 LA MAGIA: Buscamos si ya existe una calificación para este alumno, materia y trimestre
        Optional<Calificacion> calificacionExistente = calificacionRepository
                .findByAlumnoIdAndAsignacionIdAndPeriodo(dto.getAlumnoId(), dto.getAsignacionId(), dto.getPeriodo());

        Calificacion c;
        if (calificacionExistente.isPresent()) {
            // Si ya existe, la actualizamos (no creamos basura)
            c = calificacionExistente.get();
            c.setCalificacion(dto.getCalificacion());
            c.setObservaciones(dto.getObservaciones());
            c.setActivo(true);
        } else {
            // Si no existe, la creamos
            c = calificacionMapper.toEntity(dto, asignacion, alumno);
        }

        Calificacion guardada = calificacionRepository.save(c);
        return calificacionMapper.toResponseDTO(guardada);
    }

    @Override
    public CalificacionResponseDTO actualizar(Long id, CalificacionRequestDTO dto) {
        Calificacion c = calificacionRepository.findById(id)
                .orElseThrow(() -> new CalificacionNoEncontradaException(id));

        calificacionMapper.updateEntity(c, dto);
        Calificacion actualizada = calificacionRepository.save(c);
        return calificacionMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Calificacion c = calificacionRepository.findById(id)
                .orElseThrow(() -> new CalificacionNoEncontradaException(id));
        c.setActivo(false);
        calificacionRepository.save(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> listarPorAlumno(Long alumnoId) {
        if (!alumnoRepository.existsById(alumnoId)) {
            throw new AlumnoNoEncontradoException(alumnoId);
        }
        return calificacionRepository.findActivasByAlumnoId(alumnoId).stream()
                .map(calificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> listarPorAsignacion(Long asignacionId) {
        if (!asignacionRepository.existsById(asignacionId)) {
            throw new AsignacionNoEncontradaException(asignacionId);
        }
        return calificacionRepository.findActivasByAsignacionId(asignacionId).stream()
                .map(calificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioAlumno(Long alumnoId) {
        return calificacionRepository.calcularPromedioByAlumnoId(alumnoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioAsignacion(Long asignacionId) {
        return calificacionRepository.calcularPromedioByAsignacionId(asignacionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> buscarPorTermino(String termino) {
        return calificacionRepository.buscarPorTermino(termino).stream()
                .map(calificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}