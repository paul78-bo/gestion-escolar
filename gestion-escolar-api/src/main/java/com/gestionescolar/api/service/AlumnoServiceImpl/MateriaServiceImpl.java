package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.MateriaRequestDTO;
import com.gestionescolar.api.dto.MateriaResponseDTO;
import com.gestionescolar.api.entity.Materia;
import com.gestionescolar.api.exception.MateriaNoEncontradaException;
import com.gestionescolar.api.mapper.MateriaMapper;
import com.gestionescolar.api.repository.MateriaRepository;
import com.gestionescolar.api.service.MateriaService;

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
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final MateriaMapper materiaMapper;

    public MateriaServiceImpl(MateriaRepository materiaRepository, MateriaMapper materiaMapper) {
        this.materiaRepository = materiaRepository;
        this.materiaMapper = materiaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaResponseDTO> listarTodos() {
        return materiaRepository.findAll().stream().map(materiaMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaResponseDTO> listarActivas() {
        return materiaRepository.findByActivoTrue().stream().map(materiaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MateriaResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return materiaRepository.findByActivoTrue(pageable).map(materiaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MateriaResponseDTO obtenerPorId(Long id) {
        Materia m = materiaRepository.findById(id).orElseThrow(() -> new MateriaNoEncontradaException(id));
        return materiaMapper.toResponseDTO(m);
    }

    @Override
    public MateriaResponseDTO crear(MateriaRequestDTO dto) {
        if (materiaRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Ya existe una materia con el código: " + dto.getCodigo());
        }
        Materia m = materiaMapper.toEntity(dto);
        Materia guardada = materiaRepository.save(m);
        return materiaMapper.toResponseDTO(guardada);
    }

    @Override
    public MateriaResponseDTO actualizar(Long id, MateriaRequestDTO dto) {
        Materia m = materiaRepository.findById(id).orElseThrow(() -> new MateriaNoEncontradaException(id));

        if (!m.getCodigo().equals(dto.getCodigo()) && materiaRepository.existsByCodigo(dto.getCodigo())) {
            throw new RuntimeException("Ya existe una materia con el código: " + dto.getCodigo());
        }

        materiaMapper.updateEntity(m, dto);
        Materia actualizada = materiaRepository.save(m);
        return materiaMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Materia m = materiaRepository.findById(id).orElseThrow(() -> new MateriaNoEncontradaException(id));
        m.setActivo(false);
        materiaRepository.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaResponseDTO> buscarPorTermino(String termino) {
        return materiaRepository.buscarPorTermino(termino).stream().map(materiaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}