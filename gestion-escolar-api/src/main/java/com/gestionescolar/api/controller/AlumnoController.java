package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.AlumnoRequestDTO;
import com.gestionescolar.api.dto.AlumnoResponseDTO;
import com.gestionescolar.api.security.UsuarioDetails;
import com.gestionescolar.api.service.AlumnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/alumnos")
@Tag(name = "Alumnos", description = "API para gestionar alumnos")
public class AlumnoController {

	private final AlumnoService alumnoService;

	public AlumnoController(AlumnoService alumnoService) {
		this.alumnoService = alumnoService;
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR')")
	public ResponseEntity<List<AlumnoResponseDTO>> listarTodos() {
		return ResponseEntity.ok(alumnoService.listarTodos());
	}

	@GetMapping("/activos")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR')")
	public ResponseEntity<List<AlumnoResponseDTO>> listarActivos() {
		return ResponseEntity.ok(alumnoService.listarActivos());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR') or " +
			"(hasRole('ALUMNO') and @authChecker.esMismoAlumno(authentication, #id))")
	public ResponseEntity<AlumnoResponseDTO> obtenerPorId(@PathVariable Long id) {
		return ResponseEntity.ok(alumnoService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
	public ResponseEntity<AlumnoResponseDTO> crear(@Valid @RequestBody AlumnoRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(alumnoService.crear(dto));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
	public ResponseEntity<AlumnoResponseDTO> actualizar(@PathVariable Long id,
			@Valid @RequestBody AlumnoRequestDTO dto) {
		return ResponseEntity.ok(alumnoService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		alumnoService.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/buscar")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR')")
	public ResponseEntity<List<AlumnoResponseDTO>> buscar(@RequestParam String termino) {
		return ResponseEntity.ok(alumnoService.buscarPorNombreOApellido(termino));
	}

	@GetMapping("/paginado")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR')")
	public ResponseEntity<Page<AlumnoResponseDTO>> listarPaginado(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		if (page < 0)
			page = 0;
		if (size < 1)
			size = 10;
		if (size > 100)
			size = 100;
		if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc"))
			sortDir = "asc";

		Set<String> camposValidos = Set.of("id", "nombre", "apellidoP", "apellidoM", "email", "matricula", "estatus",
				"fechaIngreso", "fechaCreacion");
		if (!camposValidos.contains(sortBy))
			sortBy = "id";

		return ResponseEntity.ok(alumnoService.listarPaginado(page, size, sortBy, sortDir));
	}

	@GetMapping("/activos/paginado")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR')")
	public ResponseEntity<Page<AlumnoResponseDTO>> listarActivosPaginado(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(alumnoService.listarActivosPaginado(page, size));
	}

	@GetMapping("/buscar/paginado")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR')")
	public ResponseEntity<Page<AlumnoResponseDTO>> buscarPaginado(@RequestParam String termino,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(alumnoService.buscarPaginado(termino, page, size));
	}
	
	
	@GetMapping("/me")
	@PreAuthorize("hasRole('ALUMNO')")
	public ResponseEntity<AlumnoResponseDTO> obtenerMiPerfil() {
	    UsuarioDetails user = (UsuarioDetails) SecurityContextHolder.getContext()
	            .getAuthentication().getPrincipal();
	    return ResponseEntity.ok(alumnoService.obtenerPorId(user.getAlumnoId()));
	}
}