package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.AlumnoRequestDTO;
import com.gestionescolar.api.dto.AlumnoResponseDTO;

import java.util.List;

import org.springframework.data.domain.Page;

public interface AlumnoService {

	List<AlumnoResponseDTO> listarTodos();

	List<AlumnoResponseDTO> listarActivos();

	AlumnoResponseDTO obtenerPorId(Long id);

	AlumnoResponseDTO crear(AlumnoRequestDTO dto);

	AlumnoResponseDTO actualizar(Long id, AlumnoRequestDTO dto);

	void eliminar(Long id);

	List<AlumnoResponseDTO> buscarPorNombreOApellido(String termino);

	Page<AlumnoResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

	Page<AlumnoResponseDTO> listarActivosPaginado(int page, int size);

	Page<AlumnoResponseDTO> buscarPaginado(String termino, int page, int size);
}