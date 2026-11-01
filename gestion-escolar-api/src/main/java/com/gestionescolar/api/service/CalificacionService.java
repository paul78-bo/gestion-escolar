package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.CalificacionRequestDTO;
import com.gestionescolar.api.dto.CalificacionResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CalificacionService {

    List<CalificacionResponseDTO> listarTodas();

    Page<CalificacionResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

    CalificacionResponseDTO obtenerPorId(Long id);

    CalificacionResponseDTO crear(CalificacionRequestDTO dto);

    CalificacionResponseDTO actualizar(Long id, CalificacionRequestDTO dto);

    void eliminar(Long id);

    List<CalificacionResponseDTO> listarPorAlumno(Long alumnoId);

    List<CalificacionResponseDTO> listarPorAsignacion(Long asignacionId);

    Double calcularPromedioAlumno(Long alumnoId);

    Double calcularPromedioAsignacion(Long asignacionId);

    List<CalificacionResponseDTO> buscarPorTermino(String termino);
}