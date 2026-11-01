package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.AsignacionRequestDTO;
import com.gestionescolar.api.dto.AsignacionResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AsignacionService {

    List<AsignacionResponseDTO> listarTodas();

    Page<AsignacionResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

    AsignacionResponseDTO obtenerPorId(Long id);

    AsignacionResponseDTO crear(AsignacionRequestDTO dto);

    AsignacionResponseDTO actualizar(Long id, AsignacionRequestDTO dto);

    void eliminar(Long id);

    List<AsignacionResponseDTO> listarPorGrupo(Long grupoId);

    List<AsignacionResponseDTO> listarPorProfesor(Long profesorId);

    List<AsignacionResponseDTO> listarPorMateria(Long materiaId);

    List<AsignacionResponseDTO> buscarPorTermino(String termino);
}