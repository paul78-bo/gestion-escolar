package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.CicloEscolarRequestDTO;
import com.gestionescolar.api.dto.CicloEscolarResponseDTO;
import com.gestionescolar.api.service.CicloEscolarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/ciclos")
@Tag(name = "Ciclos Escolares", description = "API para gestionar ciclos escolares")
public class CicloEscolarController {

    private final CicloEscolarService cicloService;

    public CicloEscolarController(CicloEscolarService cicloService) {
        this.cicloService = cicloService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar todos los ciclos escolares")
    public ResponseEntity<List<CicloEscolarResponseDTO>> listarTodos() {
        return ResponseEntity.ok(cicloService.listarTodos());
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar ciclos escolares activos")
    public ResponseEntity<List<CicloEscolarResponseDTO>> listarActivos() {
        return ResponseEntity.ok(cicloService.listarActivos());
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar ciclos escolares paginados")
    public ResponseEntity<Page<CicloEscolarResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) sortDir = "asc";

        Set<String> camposValidos = Set.of(
            "id", "nombre", "fechaInicio", "fechaFin", "fechaCreacion"
        );
        if (!camposValidos.contains(sortBy)) sortBy = "id";

        return ResponseEntity.ok(cicloService.listarPaginado(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Obtener un ciclo escolar por ID")
    public ResponseEntity<CicloEscolarResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cicloService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Crear un nuevo ciclo escolar")
    public ResponseEntity<CicloEscolarResponseDTO> crear(@Valid @RequestBody CicloEscolarRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cicloService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Actualizar un ciclo escolar")
    public ResponseEntity<CicloEscolarResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody CicloEscolarRequestDTO dto) {
        return ResponseEntity.ok(cicloService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Eliminar un ciclo escolar (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cicloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar ciclos escolares por término")
    public ResponseEntity<List<CicloEscolarResponseDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(cicloService.buscarPorTermino(termino));
    }
}