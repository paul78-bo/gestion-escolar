package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.CalificacionRequestDTO;
import com.gestionescolar.api.dto.CalificacionResponseDTO;
import com.gestionescolar.api.service.CalificacionService;
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
@RequestMapping("/api/calificaciones")
@Tag(name = "Calificaciones", description = "API para gestionar calificaciones de alumnos")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar todas las calificaciones")
    public ResponseEntity<List<CalificacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(calificacionService.listarTodas());
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar calificaciones paginadas")
    public ResponseEntity<Page<CalificacionResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) sortDir = "asc";

        Set<String> camposValidos = Set.of(
            "id", "calificacion", "fechaCreacion"
        );
        if (!camposValidos.contains(sortBy)) sortBy = "id";

        return ResponseEntity.ok(calificacionService.listarPaginado(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Obtener una calificación por ID")
    public ResponseEntity<CalificacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Crear una nueva calificación")
    public ResponseEntity<CalificacionResponseDTO> crear(@Valid @RequestBody CalificacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calificacionService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Actualizar una calificación")
    public ResponseEntity<CalificacionResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody CalificacionRequestDTO dto) {
        return ResponseEntity.ok(calificacionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Eliminar una calificación (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        calificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alumno/{alumnoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO') or " +
                  "(hasRole('ALUMNO') and #alumnoId == authentication.principal.alumnoId)")
    @Operation(summary = "Listar calificaciones de un alumno")
    public ResponseEntity<List<CalificacionResponseDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(calificacionService.listarPorAlumno(alumnoId));
    }

    @GetMapping("/asignacion/{asignacionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar calificaciones de una asignación")
    public ResponseEntity<List<CalificacionResponseDTO>> listarPorAsignacion(@PathVariable Long asignacionId) {
        return ResponseEntity.ok(calificacionService.listarPorAsignacion(asignacionId));
    }

    @GetMapping("/alumno/{alumnoId}/promedio")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO') or " +
                  "(hasRole('ALUMNO') and #alumnoId == authentication.principal.alumnoId)")
    @Operation(summary = "Calcular promedio de un alumno")
    public ResponseEntity<Double> calcularPromedioAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(calificacionService.calcularPromedioAlumno(alumnoId));
    }

    @GetMapping("/asignacion/{asignacionId}/promedio")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Calcular promedio de una asignación")
    public ResponseEntity<Double> calcularPromedioAsignacion(@PathVariable Long asignacionId) {
        return ResponseEntity.ok(calificacionService.calcularPromedioAsignacion(asignacionId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar calificaciones por término")
    public ResponseEntity<List<CalificacionResponseDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(calificacionService.buscarPorTermino(termino));
    }
}