package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.MateriaRequestDTO;
import com.gestionescolar.api.dto.MateriaResponseDTO;
import com.gestionescolar.api.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "API para gestionar materias")
public class MateriaController {

	private final MateriaService materiaService;

	public MateriaController(MateriaService materiaService) {
		this.materiaService = materiaService;
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Listar todas las materias")
	public ResponseEntity<List<MateriaResponseDTO>> listarTodos() {
		return ResponseEntity.ok(materiaService.listarTodos());
	}

	@GetMapping("/activas")
	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Listar materias activas")
	public ResponseEntity<List<MateriaResponseDTO>> listarActivas() {
		return ResponseEntity.ok(materiaService.listarActivas());
	}

	@GetMapping("/paginado")
	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Listar materias paginadas")
	public ResponseEntity<Page<MateriaResponseDTO>> listarPaginado(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		return ResponseEntity.ok(materiaService.listarPaginado(page, size, sortBy, sortDir));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Obtener una materia por ID")
	public ResponseEntity<MateriaResponseDTO> obtenerPorId(@PathVariable Long id) {
		return ResponseEntity.ok(materiaService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Crear una nueva materia")
	public ResponseEntity<MateriaResponseDTO> crear(@Valid @RequestBody MateriaRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.crear(dto));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Actualizar una materia existente")
	public ResponseEntity<MateriaResponseDTO> actualizar(@PathVariable Long id,
			@Valid @RequestBody MateriaRequestDTO dto) {
		return ResponseEntity.ok(materiaService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Eliminar una materia (soft delete)")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		materiaService.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/buscar")
	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
	@Operation(summary = "Buscar materias por término")
	public ResponseEntity<List<MateriaResponseDTO>> buscar(@RequestParam String termino) {
		return ResponseEntity.ok(materiaService.buscarPorTermino(termino));
	}
}