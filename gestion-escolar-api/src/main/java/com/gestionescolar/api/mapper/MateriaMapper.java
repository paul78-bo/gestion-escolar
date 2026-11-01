package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.MateriaRequestDTO;
import com.gestionescolar.api.dto.MateriaResponseDTO;
import com.gestionescolar.api.entity.Materia;
import org.springframework.stereotype.Component;

@Component
public class MateriaMapper {

    public Materia toEntity(MateriaRequestDTO dto) {
        if (dto == null) return null;
        Materia m = new Materia();
        m.setNombre(dto.getNombre());
        m.setCodigo(dto.getCodigo());
        m.setCreditos(dto.getCreditos());
        m.setHorasSemana(dto.getHorasSemana());
        
        m.setCampoFormativo(dto.getCampo()); // 👈 Corregido

        m.setDescripcion(dto.getDescripcion());
        m.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return m;
    }

    public MateriaResponseDTO toResponseDTO(Materia entity) {
        if (entity == null) return null;
        MateriaResponseDTO dto = new MateriaResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCodigo(entity.getCodigo());
        dto.setCreditos(entity.getCreditos());
        dto.setHorasSemana(entity.getHorasSemana());
        dto.setCampo(entity.getCampoFormativo()); // 👈 Corregido

        dto.setDescripcion(entity.getDescripcion());
        dto.setActivo(entity.getActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }

    public void updateEntity(Materia entity, MateriaRequestDTO dto) {
        if (entity == null || dto == null) return;
        entity.setNombre(dto.getNombre());
        entity.setCodigo(dto.getCodigo());
        entity.setCreditos(dto.getCreditos());
        entity.setHorasSemana(dto.getHorasSemana());
        entity.setDescripcion(dto.getDescripcion());
        entity.setCampoFormativo(dto.getCampo()); // 👈 Corregido

        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
}