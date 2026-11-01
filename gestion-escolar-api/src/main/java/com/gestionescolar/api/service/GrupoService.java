package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.GrupoRequestDTO;
import com.gestionescolar.api.dto.GrupoResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GrupoService {

    List<GrupoResponseDTO> listarTodos();

    List<GrupoResponseDTO> listarActivos();

    Page<GrupoResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

    GrupoResponseDTO obtenerPorId(Long id);

    GrupoResponseDTO crear(GrupoRequestDTO dto);

    GrupoResponseDTO actualizar(Long id, GrupoRequestDTO dto);

    void eliminar(Long id);

    List<GrupoResponseDTO> listarPorProfesor(Long profesorId);

    List<GrupoResponseDTO> listarPorMateria(Long materiaId);

    List<GrupoResponseDTO> buscarPorTermino(String termino);
    }