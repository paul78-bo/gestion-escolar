package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.BoletaResponseDTO;
import com.gestionescolar.api.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletas")
@Tag(name = "Boletas", description = "API para generar boletas de calificaciones")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @GetMapping("/alumno/{alumnoId}")   
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'DIRECTOR', 'SECRETARIO') or " +
            "(hasRole('ALUMNO') and @authChecker.esMismoAlumno(authentication, #alumnoId))")
    @Operation(summary = "Generar la boleta de un alumno")   
    
    public ResponseEntity<BoletaResponseDTO> generarBoleta(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(boletaService.generarBoletaPorAlumno(alumnoId));
    }
}