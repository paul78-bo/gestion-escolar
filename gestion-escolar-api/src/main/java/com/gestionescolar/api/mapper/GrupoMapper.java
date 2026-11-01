package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.GrupoRequestDTO;
import com.gestionescolar.api.dto.GrupoResponseDTO;
import com.gestionescolar.api.entity.CicloEscolar;
import com.gestionescolar.api.entity.Grupo;
import org.springframework.stereotype.Component;

@Component
public class GrupoMapper {

    public Grupo toEntity(GrupoRequestDTO dto, CicloEscolar ciclo) {
        if (dto == null || ciclo == null) return null;

        Grupo g = new Grupo();
        g.setGrado(dto.getGrado());
        g.setLetra(dto.getLetra());
        g.setNombre(dto.getGrado() + dto.getLetra());
        g.setTurno(dto.getTurno());
        g.setCapacidad(dto.getCapacidad() != null ? dto.getCapacidad() : 30);
        g.setInscritos(0);
        g.setCiclo(ciclo);
        g.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return g;
    }

    public GrupoResponseDTO toResponseDTO(Grupo entity) {
        if (entity == null) return null;

        GrupoResponseDTO dto = new GrupoResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setGrado(entity.getGrado());
        dto.setLetra(entity.getLetra());
        dto.setTurno(entity.getTurno());
        dto.setCapacidad(entity.getCapacidad());
        dto.setInscritos(entity.getInscritos());

        if (entity.getCiclo() != null) {
            dto.setCicloId(entity.getCiclo().getId());
            dto.setCicloNombre(entity.getCiclo().getNombre());
        }

        dto.setActivo(entity.getActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }

    public void updateEntity(Grupo entity, GrupoRequestDTO dto, CicloEscolar ciclo) {
        if (entity == null || dto == null) return;

        entity.setGrado(dto.getGrado());
        entity.setLetra(dto.getLetra());
        entity.setNombre(dto.getGrado() + dto.getLetra());
        entity.setTurno(dto.getTurno());
        if (dto.getCapacidad() != null) entity.setCapacidad(dto.getCapacidad());
        if (ciclo != null) entity.setCiclo(ciclo);
        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
}