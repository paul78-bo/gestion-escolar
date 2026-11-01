package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.ProfesorRequestDTO;
import com.gestionescolar.api.dto.ProfesorResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProfesorService {

    List<ProfesorResponseDTO> listarTodos();

    List<ProfesorResponseDTO> listarActivos();

    Page<ProfesorResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

    ProfesorResponseDTO obtenerPorId(Long id);

    ProfesorResponseDTO crear(ProfesorRequestDTO dto);

    ProfesorResponseDTO actualizar(Long id, ProfesorRequestDTO dto);

    void eliminar(Long id);

    List<ProfesorResponseDTO> buscarPorTermino(String termino);

    Page<ProfesorResponseDTO> buscarPaginado(String termino, int page, int size);
}