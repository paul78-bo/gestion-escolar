package com.gestionescolar.api.dto;

import java.util.List;

public class CampoFormativoDTO {
    private String nombre;
    private List<MateriaCalificacionDTO> materias;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<MateriaCalificacionDTO> getMaterias() { return materias; }
    public void setMaterias(List<MateriaCalificacionDTO> materias) { this.materias = materias; }
}