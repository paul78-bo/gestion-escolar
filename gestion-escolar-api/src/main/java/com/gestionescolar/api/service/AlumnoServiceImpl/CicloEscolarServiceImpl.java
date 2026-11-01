package com.gestionescolar.api.service.AlumnoServiceImpl;

import com.gestionescolar.api.dto.CicloEscolarRequestDTO;
import com.gestionescolar.api.dto.CicloEscolarResponseDTO;
import com.gestionescolar.api.entity.CicloEscolar;
import com.gestionescolar.api.exception.CicloEscolarNoEncontradoException;
import com.gestionescolar.api.mapper.CicloEscolarMapper;
import com.gestionescolar.api.repository.CicloEscolarRepository;
import com.gestionescolar.api.service.CicloEscolarService;

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
public class CicloEscolarServiceImpl implements CicloEscolarService {

    private final CicloEscolarRepository cicloRepository;
    private final CicloEscolarMapper cicloMapper;

    public CicloEscolarServiceImpl(CicloEscolarRepository cicloRepository,
                                    CicloEscolarMapper cicloMapper) {
        this.cicloRepository = cicloRepository;
        this.cicloMapper = cicloMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CicloEscolarResponseDTO> listarTodos() {
        return cicloRepository.findAll().stream()
                .map(cicloMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CicloEscolarResponseDTO> listarActivos() {
        return cicloRepository.findByActivoTrue().stream()
                .map(cicloMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CicloEscolarResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return cicloRepository.findByActivoTrue(pageable).map(cicloMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CicloEscolarResponseDTO obtenerPorId(Long id) {
        CicloEscolar c = cicloRepository.findById(id)
                .orElseThrow(() -> new CicloEscolarNoEncontradoException(id));
        return cicloMapper.toResponseDTO(c);
    }

    @Override
    public CicloEscolarResponseDTO crear(CicloEscolarRequestDTO dto) {
        if (cicloRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un ciclo escolar con el nombre: " + dto.getNombre());
        }

        CicloEscolar c = cicloMapper.toEntity(dto);
        CicloEscolar guardado = cicloRepository.save(c);
        return cicloMapper.toResponseDTO(guardado);
    }

    @Override
    public CicloEscolarResponseDTO actualizar(Long id, CicloEscolarRequestDTO dto) {
        CicloEscolar c = cicloRepository.findById(id)
                .orElseThrow(() -> new CicloEscolarNoEncontradoException(id));

        if (!c.getNombre().equals(dto.getNombre()) && cicloRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un ciclo escolar con el nombre: " + dto.getNombre());
        }

        cicloMapper.updateEntity(c, dto);
        CicloEscolar actualizado = cicloRepository.save(c);
        return cicloMapper.toResponseDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        CicloEscolar c = cicloRepository.findById(id)
                .orElseThrow(() -> new CicloEscolarNoEncontradoException(id));
        c.setActivo(false);
        cicloRepository.save(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CicloEscolarResponseDTO> buscarPorTermino(String termino) {
        return cicloRepository.buscarPorTermino(termino).stream()
                .map(cicloMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}