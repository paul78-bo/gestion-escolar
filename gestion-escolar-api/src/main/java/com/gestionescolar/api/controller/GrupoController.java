package com.gestionescolar.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.gestionescolar.api.dto.GrupoRequestDTO;
import com.gestionescolar.api.dto.GrupoResponseDTO;
import com.gestionescolar.api.service.GrupoService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "Grupos", description = "API para gestionar grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar todos los grupos")
    public ResponseEntity<List<GrupoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(grupoService.listarTodos());
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar grupos activos")
    public ResponseEntity<List<GrupoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(grupoService.listarActivos());
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar grupos paginados")
    public ResponseEntity<Page<GrupoResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) sortDir = "asc";

        Set<String> camposValidos = Set.of(
            "id", "nombre", "grado", "letra", "turno",
            "capacidad", "inscritos", "fechaCreacion"
        );
        if (!camposValidos.contains(sortBy)) sortBy = "id";

        return ResponseEntity.ok(grupoService.listarPaginado(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Obtener un grupo por ID")
    public ResponseEntity<GrupoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Crear un nuevo grupo")
    public ResponseEntity<GrupoResponseDTO> crear(@Valid @RequestBody GrupoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Actualizar un grupo")
    public ResponseEntity<GrupoResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody GrupoRequestDTO dto) {
        return ResponseEntity.ok(grupoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Eliminar un grupo (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        grupoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profesor/{profesorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar grupos de un profesor (vía asignaciones)")
    public ResponseEntity<List<GrupoResponseDTO>> listarPorProfesor(@PathVariable Long profesorId) {
        return ResponseEntity.ok(grupoService.listarPorProfesor(profesorId));
    }

    @GetMapping("/materia/{materiaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar grupos de una materia (vía asignaciones)")
    public ResponseEntity<List<GrupoResponseDTO>> listarPorMateria(@PathVariable Long materiaId) {
        return ResponseEntity.ok(grupoService.listarPorMateria(materiaId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar grupos por término")
    public ResponseEntity<List<GrupoResponseDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(grupoService.buscarPorTermino(termino));
    }
}