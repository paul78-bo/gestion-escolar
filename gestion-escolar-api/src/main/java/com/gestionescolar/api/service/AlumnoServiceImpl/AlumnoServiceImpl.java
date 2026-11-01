package com.gestionescolar.api.service.AlumnoServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestionescolar.api.dto.AlumnoRequestDTO;
import com.gestionescolar.api.dto.AlumnoResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.exception.AlumnoNoEncontradoException;
import com.gestionescolar.api.mapper.AlumnoMapper;
import com.gestionescolar.api.repository.AlumnoRepository;
import com.gestionescolar.api.repository.UsuarioRepository;
import com.gestionescolar.api.service.AlumnoService;

@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;
    private final UsuarioRepository usuarioRepository;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository, AlumnoMapper alumnoMapper,
                             UsuarioRepository usuarioRepository) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoMapper = alumnoMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> listarTodos() {
        return alumnoRepository.findAll().stream().map(alumnoMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> listarActivos() {
        return alumnoRepository.findByActivoTrue().stream().map(alumnoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponseDTO obtenerPorId(Long id) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new AlumnoNoEncontradoException(id));
        return alumnoMapper.toResponseDTO(alumno);
    }

    @Override
    public AlumnoResponseDTO crear(AlumnoRequestDTO dto) {
        if (alumnoRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un alumno con el email: " + dto.getEmail());
        }
        if (alumnoRepository.existsByMatricula(dto.getMatricula())) {
            throw new RuntimeException("Ya existe un alumno con la matrícula: " + dto.getMatricula());
        }

        Usuario usuario = null;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        Alumno alumno = alumnoMapper.toEntity(dto, usuario);
        Alumno guardado = alumnoRepository.save(alumno);
        return alumnoMapper.toResponseDTO(guardado);
    }

    @Override
    public AlumnoResponseDTO actualizar(Long id, AlumnoRequestDTO dto) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new AlumnoNoEncontradoException(id));

        if (!alumno.getEmail().equals(dto.getEmail()) && alumnoRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un alumno con el email: " + dto.getEmail());
        }

        if (!alumno.getMatricula().equals(dto.getMatricula())
                && alumnoRepository.existsByMatricula(dto.getMatricula())) {
            throw new RuntimeException("Ya existe un alumno con la matrícula: " + dto.getMatricula());
        }

        Usuario usuario = null;
        if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));
        }

        alumnoMapper.updateEntity(alumno, dto, usuario);
        Alumno actualizado = alumnoRepository.save(alumno);
        return alumnoMapper.toResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new AlumnoNoEncontradoException(id));
        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }
    
   

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> buscarPorNombreOApellido(String termino) {
        return alumnoRepository.buscarPorTermino(termino).stream().map(alumnoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlumnoResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Alumno> paginaAlumnos = alumnoRepository.findByActivoTrue(pageable);
        return paginaAlumnos.map(alumnoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlumnoResponseDTO> listarActivosPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<Alumno> pagina = alumnoRepository.findByActivoTrue(pageable);
        return pagina.map(alumnoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlumnoResponseDTO> buscarPaginado(String termino, int page, int size) {
        // Buscamos todos los resultados y paginamos en memoria
        List<Alumno> resultados = alumnoRepository.buscarPorTermino(termino);

        Pageable pageable = PageRequest.of(page, size);
        int inicio = (int) pageable.getOffset();
        int fin = Math.min(inicio + pageable.getPageSize(), resultados.size());

        List<AlumnoResponseDTO> content = resultados.subList(inicio, fin).stream()
                .map(alumnoMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, resultados.size());
    }
}