package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.CicloEscolarRequestDTO;
import com.gestionescolar.api.dto.CicloEscolarResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CicloEscolarService {

    List<CicloEscolarResponseDTO> listarTodos();

    List<CicloEscolarResponseDTO> listarActivos();

    Page<CicloEscolarResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

    CicloEscolarResponseDTO obtenerPorId(Long id);

    CicloEscolarResponseDTO crear(CicloEscolarRequestDTO dto);

    CicloEscolarResponseDTO actualizar(Long id, CicloEscolarRequestDTO dto);

    void eliminar(Long id);

    List<CicloEscolarResponseDTO> buscarPorTermino(String termino);
}