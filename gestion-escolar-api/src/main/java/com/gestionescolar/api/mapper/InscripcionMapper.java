package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.InscripcionRequestDTO;
import com.gestionescolar.api.dto.InscripcionResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.entity.Inscripcion;
import com.gestionescolar.api.enums.EstatusInscripcion;
import org.springframework.stereotype.Component;

@Component
public class InscripcionMapper {

    public Inscripcion toEntity(InscripcionRequestDTO dto, Alumno alumno, Grupo grupo) {
        if (dto == null || alumno == null || grupo == null) return null;

        Inscripcion i = new Inscripcion();
        i.setAlumno(alumno);
        i.setGrupo(grupo);
        i.setFechaInscripcion(dto.getFechaInscripcion());
        i.setEstatus(dto.getEstatus() != null ? dto.getEstatus() : EstatusInscripcion.ACTIVA);
        i.setActivo(true);
        return i;
    }

    public InscripcionResponseDTO toResponseDTO(Inscripcion entity) {
        if (entity == null) return null;

        InscripcionResponseDTO dto = new InscripcionResponseDTO();
        dto.setId(entity.getId());

        if (entity.getAlumno() != null) {
            dto.setAlumnoId(entity.getAlumno().getId());
            dto.setAlumnoNombreCompleto(
                entity.getAlumno().getNombre() + " " +
                entity.getAlumno().getApellidoP() + " " +
                entity.getAlumno().getApellidoM()
            );
            dto.setAlumnoMatricula(entity.getAlumno().getMatricula());
        }

        if (entity.getGrupo() != null) {
            dto.setGrupoId(entity.getGrupo().getId());
            dto.setGrupoNombre(entity.getGrupo().getNombre());
            dto.setGrupoGrado(entity.getGrupo().getGrado());
            dto.setGrupoLetra(entity.getGrupo().getLetra());
            dto.setGrupoTurno(entity.getGrupo().getTurno());

            if (entity.getGrupo().getCiclo() != null) {
                dto.setCicloId(entity.getGrupo().getCiclo().getId());
                dto.setCicloNombre(entity.getGrupo().getCiclo().getNombre());
            }
        }

        dto.setFechaInscripcion(entity.getFechaInscripcion());
        dto.setEstatus(entity.getEstatus());
        dto.setActivo(entity.getActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }

    public void updateEntity(Inscripcion entity, InscripcionRequestDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getFechaInscripcion() != null) entity.setFechaInscripcion(dto.getFechaInscripcion());
        if (dto.getEstatus() != null) entity.setEstatus(dto.getEstatus());
        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
}