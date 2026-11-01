package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.InscripcionRequestDTO;
import com.gestionescolar.api.dto.InscripcionResponseDTO;

import java.util.List;

import org.springframework.data.domain.Page;

public interface InscripcionService {

	List<InscripcionResponseDTO> listarTodas();

	InscripcionResponseDTO obtenerPorId(Long id);

	InscripcionResponseDTO crear(InscripcionRequestDTO dto);

	InscripcionResponseDTO actualizar(Long id, InscripcionRequestDTO dto);

	void eliminar(Long id);

	List<InscripcionResponseDTO> listarPorAlumno(Long alumnoId);

	List<InscripcionResponseDTO> listarPorMateria(Long materiaId);

	long contarAlumnosPorMateria(Long materiaId);

	Double calcularPromedioAlumno(Long alumnoId);

	List<InscripcionResponseDTO> obtenerHistorial(Long alumnoId, Long materiaId);

	List<InscripcionResponseDTO> listarPorGrupo(Long grupoId);

	Page<InscripcionResponseDTO> listarPaginado(int page, int size, String sortBy, String sortDir);

	List<InscripcionResponseDTO> buscarPorTermino(String termino);
	
	
	
	
	
}