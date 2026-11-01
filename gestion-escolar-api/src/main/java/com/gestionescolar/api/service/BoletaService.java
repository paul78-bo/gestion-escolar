package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.BoletaResponseDTO;

public interface BoletaService {
    // Genera la boleta de un alumno en el ciclo escolar activo
    BoletaResponseDTO generarBoletaPorAlumno(Long alumnoId);
}