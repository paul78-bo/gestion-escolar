package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.InscripcionRequestDTO;
import com.gestionescolar.api.dto.InscripcionResponseDTO;
import com.gestionescolar.api.service.InscripcionService;
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
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "API para gestionar inscripciones de alumnos a grupos")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar todas las inscripciones")
    public ResponseEntity<List<InscripcionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(inscripcionService.listarTodas());
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar inscripciones paginadas")
    public ResponseEntity<Page<InscripcionResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) sortDir = "asc";

        Set<String> camposValidos = Set.of(
            "id", "fechaInscripcion", "estatus", "fechaCreacion"
        );
        if (!camposValidos.contains(sortBy)) sortBy = "id";

        return ResponseEntity.ok(inscripcionService.listarPaginado(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Obtener una inscripción por ID")
    public ResponseEntity<InscripcionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Crear una nueva inscripción (auto-crea calificaciones)")
    public ResponseEntity<InscripcionResponseDTO> crear(@Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Actualizar una inscripción")
    public ResponseEntity<InscripcionResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.ok(inscripcionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Eliminar una inscripción (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alumno/{alumnoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO') or " +
                  "(hasRole('ALUMNO') and #alumnoId == authentication.principal.alumnoId)")
    @Operation(summary = "Listar inscripciones de un alumno")
    public ResponseEntity<List<InscripcionResponseDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(inscripcionService.listarPorAlumno(alumnoId));
    }

    @GetMapping("/grupo/{grupoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Listar inscripciones de un grupo")
    public ResponseEntity<List<InscripcionResponseDTO>> listarPorGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(inscripcionService.listarPorGrupo(grupoId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR','ALUMNO', 'DIRECTOR', 'SECRETARIO')")
    @Operation(summary = "Buscar inscripciones por término")
    public ResponseEntity<List<InscripcionResponseDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(inscripcionService.buscarPorTermino(termino));
    }
}