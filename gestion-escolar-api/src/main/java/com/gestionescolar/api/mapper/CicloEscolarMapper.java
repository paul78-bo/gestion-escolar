package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.CicloEscolarRequestDTO;
import com.gestionescolar.api.dto.CicloEscolarResponseDTO;
import com.gestionescolar.api.entity.CicloEscolar;
import org.springframework.stereotype.Component;

@Component
public class CicloEscolarMapper {

	public CicloEscolar toEntity(CicloEscolarRequestDTO dto) {
		if (dto == null)
			return null;

		CicloEscolar c = new CicloEscolar();
		c.setNombre(dto.getNombre());
		c.setFechaInicio(dto.getFechaInicio());
		c.setFechaFin(dto.getFechaFin());
		c.setNivelEducativo(dto.getNivelEducativo() != null ? dto.getNivelEducativo() : "TELESECUNDARIA");
		c.setTipoPeriodo(dto.getTipoPeriodo() != null ? dto.getTipoPeriodo() : "ANUAL");
		c.setDescripcion(dto.getDescripcion());
		c.setObservaciones(dto.getObservaciones());
		c.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
		return c;
	}

	public CicloEscolarResponseDTO toResponseDTO(CicloEscolar entity) {
		if (entity == null)
			return null;

		CicloEscolarResponseDTO dto = new CicloEscolarResponseDTO();
		dto.setId(entity.getId());
		dto.setNombre(entity.getNombre());
		dto.setFechaInicio(entity.getFechaInicio());
		dto.setFechaFin(entity.getFechaFin());
		dto.setNivelEducativo(entity.getNivelEducativo());
		dto.setTipoPeriodo(entity.getTipoPeriodo());
		dto.setDescripcion(entity.getDescripcion());
		dto.setObservaciones(entity.getObservaciones());
		dto.setActivo(entity.getActivo());
		dto.setFechaCreacion(entity.getFechaCreacion());
		dto.setFechaActualizacion(entity.getFechaActualizacion());
		return dto;
	}

	public void updateEntity(CicloEscolar entity, CicloEscolarRequestDTO dto) {
		if (entity == null || dto == null)
			return;

		entity.setNombre(dto.getNombre());
		entity.setFechaInicio(dto.getFechaInicio());
		entity.setFechaFin(dto.getFechaFin());
		entity.setNivelEducativo(dto.getNivelEducativo());
		entity.setTipoPeriodo(dto.getTipoPeriodo());
		entity.setDescripcion(dto.getDescripcion());
		entity.setObservaciones(dto.getObservaciones());
		if (dto.getActivo() != null)
			entity.setActivo(dto.getActivo());
	}
}