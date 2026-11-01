package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.CalificacionRequestDTO;
import com.gestionescolar.api.dto.CalificacionResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.entity.Calificacion;
import org.springframework.stereotype.Component;

@Component
public class CalificacionMapper {

    public Calificacion toEntity(CalificacionRequestDTO dto, Asignacion asignacion, Alumno alumno) {
        if (dto == null || asignacion == null || alumno == null) return null;

        Calificacion c = new Calificacion();
        
        c.setPeriodo(dto.getPeriodo());
        c.setAsignacion(asignacion);
        c.setAlumno(alumno);
        c.setCalificacion(dto.getCalificacion());
        c.setObservaciones(dto.getObservaciones());
        c.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return c;
    }
    
    
     // En updateEntity:
    
    public CalificacionResponseDTO toResponseDTO(Calificacion entity) {
        if (entity == null) return null;

        CalificacionResponseDTO dto = new CalificacionResponseDTO();
        dto.setId(entity.getId());

        if (entity.getAsignacion() != null) {
            dto.setAsignacionId(entity.getAsignacion().getId());

            if (entity.getAsignacion().getMateria() != null) {
                dto.setMateriaNombre(entity.getAsignacion().getMateria().getNombre());
            }
            if (entity.getAsignacion().getGrupo() != null) {
                dto.setGrupoNombre(entity.getAsignacion().getGrupo().getNombre());
            }
        }

        if (entity.getAlumno() != null) {
            dto.setAlumnoId(entity.getAlumno().getId());
            dto.setAlumnoNombreCompleto(
                entity.getAlumno().getNombre() + " " +
                entity.getAlumno().getApellidoP() + " " +
                entity.getAlumno().getApellidoM()
            );
            dto.setAlumnoMatricula(entity.getAlumno().getMatricula());
        }
        
        dto.setPeriodo(entity.getPeriodo()); // 👈 AGREGA ESTA LÍNEA
        dto.setCalificacion(entity.getCalificacion());
        dto.setObservaciones(entity.getObservaciones());
        dto.setActivo(entity.getActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }

    public void updateEntity(Calificacion entity, CalificacionRequestDTO dto) {
        if (entity == null || dto == null) return;

        entity.setCalificacion(dto.getCalificacion());
        entity.setPeriodo(dto.getPeriodo());
        entity.setObservaciones(dto.getObservaciones());
        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
    
    
}