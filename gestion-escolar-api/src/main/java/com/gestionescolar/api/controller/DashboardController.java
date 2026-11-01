package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.DashboardResponseDTO;
import com.gestionescolar.api.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "API para contadores del dashboard")
public class DashboardController {

	private final AlumnoRepository alumnoRepository;
	private final ProfesorRepository profesorRepository;
	private final MateriaRepository materiaRepository;
	private final GrupoRepository grupoRepository;
	private final CicloEscolarRepository cicloRepository;
	private final AsignacionRepository asignacionRepository;
	private final InscripcionRepository inscripcionRepository;
	private final CalificacionRepository calificacionRepository;

	public DashboardController(AlumnoRepository alumnoRepository, ProfesorRepository profesorRepository,
			MateriaRepository materiaRepository, GrupoRepository grupoRepository,
			CicloEscolarRepository cicloRepository, AsignacionRepository asignacionRepository,
			InscripcionRepository inscripcionRepository, CalificacionRepository calificacionRepository) {
		this.alumnoRepository = alumnoRepository;
		this.profesorRepository = profesorRepository;
		this.materiaRepository = materiaRepository;
		this.grupoRepository = grupoRepository;
		this.cicloRepository = cicloRepository;
		this.asignacionRepository = asignacionRepository;
		this.inscripcionRepository = inscripcionRepository;
		this.calificacionRepository = calificacionRepository;
	}

	@GetMapping("/contadores")
	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Obtener todos los contadores del dashboard")
	public ResponseEntity<DashboardResponseDTO> obtenerContadores() {
		DashboardResponseDTO dto = new DashboardResponseDTO();

		// Totales (todos, activos e inactivos)
		dto.setTotalAlumnos(alumnoRepository.count());
		dto.setTotalProfesores(profesorRepository.count());
		dto.setTotalMaterias(materiaRepository.count());
		dto.setTotalGrupos(grupoRepository.count());
		dto.setTotalCiclos(cicloRepository.count());
		dto.setTotalAsignaciones(asignacionRepository.count());
		dto.setTotalInscripciones(inscripcionRepository.count());
		dto.setTotalCalificaciones(calificacionRepository.count());

		// Activos
		dto.setAlumnosActivos((long) alumnoRepository.findByActivoTrue().size());
		dto.setProfesoresActivos((long) profesorRepository.findByActivoTrue().size());
		dto.setMateriasActivas((long) materiaRepository.findByActivoTrue().size());
		dto.setGruposActivos((long) grupoRepository.findByActivoTrue().size());
		dto.setCiclosActivos((long) cicloRepository.findByActivoTrue().size());

		return ResponseEntity.ok(dto);
	}
}