package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.AsignacionRequestDTO;
import com.gestionescolar.api.dto.AsignacionResponseDTO;
import com.gestionescolar.api.service.AsignacionService;
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
@RequestMapping("/api/asignaciones")
@Tag(name = "Asignaciones", description = "API para gestionar asignaciones de materia+profesor a grupos")
public class AsignacionController {

    private final AsignacionService asignacionService;

    public AsignacionController(AsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO','DIRECTOR','SECRETARIO')")
    @Operation(summary = "Listar todas las asignaciones")
    public ResponseEntity<List<AsignacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(asignacionService.listarTodas());
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar asignaciones paginadas")
    public ResponseEntity<Page<AsignacionResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) sortDir = "asc";

        Set<String> camposValidos = Set.of(
            "id", "aula", "horario", "fechaCreacion"
        );
        if (!camposValidos.contains(sortBy)) sortBy = "id";

        return ResponseEntity.ok(asignacionService.listarPaginado(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Obtener una asignación por ID")
    public ResponseEntity<AsignacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asignacionService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Crear una nueva asignación (auto-crea calificaciones para alumnos ya inscritos)")
    public ResponseEntity<AsignacionResponseDTO> crear(@Valid @RequestBody AsignacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asignacionService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Actualizar una asignación")
    public ResponseEntity<AsignacionResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody AsignacionRequestDTO dto) {
        return ResponseEntity.ok(asignacionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Eliminar una asignación (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grupo/{grupoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar asignaciones de un grupo")
    public ResponseEntity<List<AsignacionResponseDTO>> listarPorGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(asignacionService.listarPorGrupo(grupoId));
    }

    @GetMapping("/profesor/{profesorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO') or " +
                  "(hasRole('PROFESOR') and #profesorId == authentication.principal.profesorId)")
    @Operation(summary = "Listar asignaciones de un profesor")
    public ResponseEntity<List<AsignacionResponseDTO>> listarPorProfesor(@PathVariable Long profesorId) {
        return ResponseEntity.ok(asignacionService.listarPorProfesor(profesorId));
    }

    @GetMapping("/materia/{materiaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar asignaciones de una materia")
    public ResponseEntity<List<AsignacionResponseDTO>> listarPorMateria(@PathVariable Long materiaId) {
        return ResponseEntity.ok(asignacionService.listarPorMateria(materiaId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar asignaciones por término")
    public ResponseEntity<List<AsignacionResponseDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(asignacionService.buscarPorTermino(termino));
    }
}