package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.MateriaRequestDTO;
import com.gestionescolar.api.dto.MateriaResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MateriaService {

    List<MateriaResponseDTO> listarTodos();

    List<MateriaResponseDTO> listarActivas();

    Page<MateriaResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

    MateriaResponseDTO obtenerPorId(Long id);

    MateriaResponseDTO crear(MateriaRequestDTO dto);

    MateriaResponseDTO actualizar(Long id, MateriaRequestDTO dto);

    void eliminar(Long id);

    List<MateriaResponseDTO> buscarPorTermino(String termino);
}