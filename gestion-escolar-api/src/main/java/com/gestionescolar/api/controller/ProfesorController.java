package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.ProfesorRequestDTO;
import com.gestionescolar.api.dto.ProfesorResponseDTO;
import com.gestionescolar.api.service.ProfesorService;
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
@RequestMapping("/api/profesores")
@Tag(name = "Profesores", description = "API para gestionar profesores")
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar todos los profesores")
    public ResponseEntity<List<ProfesorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(profesorService.listarTodos());
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar profesores activos")
    public ResponseEntity<List<ProfesorResponseDTO>> listarActivos() {
        return ResponseEntity.ok(profesorService.listarActivos());
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar profesores paginados")
    public ResponseEntity<Page<ProfesorResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(profesorService.listarPaginado(page, size, sortBy, sortDir));
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Obtener un profesor por ID")
    public ResponseEntity<ProfesorResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Crear un nuevo profesor")
    public ResponseEntity<ProfesorResponseDTO> crear(@Valid @RequestBody ProfesorRequestDTO dto) {
        ProfesorResponseDTO creado = profesorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Actualizar un profesor existente")
    public ResponseEntity<ProfesorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesorRequestDTO dto) {
        return ResponseEntity.ok(profesorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Eliminar un profesor (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar profesores por término")
    public ResponseEntity<List<ProfesorResponseDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(profesorService.buscarPorTermino(termino));
    }

    @GetMapping("/buscar/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar profesores paginados")
    public ResponseEntity<Page<ProfesorResponseDTO>> buscarPaginado(
            @RequestParam String termino,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(profesorService.buscarPaginado(termino, page, size));
    }
    
    
    
    
    
}