package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.AsignacionRequestDTO;
import com.gestionescolar.api.dto.AsignacionResponseDTO;
import com.gestionescolar.api.entity.Asignacion;
import com.gestionescolar.api.entity.Grupo;
import com.gestionescolar.api.entity.Materia;
import com.gestionescolar.api.entity.Profesor;
import org.springframework.stereotype.Component;

@Component
public class AsignacionMapper {

	public Asignacion toEntity(AsignacionRequestDTO dto, Grupo grupo, Materia materia, Profesor profesor) {
		if (dto == null || grupo == null || materia == null || profesor == null)
			return null;

		Asignacion a = new Asignacion();
		a.setGrupo(grupo);
		a.setMateria(materia);
		a.setProfesor(profesor);
		a.setAula(dto.getAula());

		// 👇 NUEVOS CAMPOS DE HORARIO
		a.setDiaSemana(dto.getDiaSemana());
		a.setHoraInicio(dto.getHoraInicio());
		a.setHoraFin(dto.getHoraFin());

		a.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
		return a;
	}

	public AsignacionResponseDTO toResponseDTO(Asignacion entity) {
		if (entity == null)
			return null;

		AsignacionResponseDTO dto = new AsignacionResponseDTO();
		dto.setId(entity.getId());

		if (entity.getGrupo() != null) {
			dto.setGrupoId(entity.getGrupo().getId());
			dto.setGrupoNombre(entity.getGrupo().getNombre());
			dto.setGrupoGrado(entity.getGrupo().getGrado());
			dto.setGrupoLetra(entity.getGrupo().getLetra());
		}

		if (entity.getMateria() != null) {
			dto.setMateriaId(entity.getMateria().getId());
			dto.setMateriaNombre(entity.getMateria().getNombre());
			dto.setMateriaCodigo(entity.getMateria().getCodigo());
		}

		if (entity.getProfesor() != null) {
			dto.setProfesorId(entity.getProfesor().getId());
			dto.setProfesorNombreCompleto(entity.getProfesor().getNombre() + " " + entity.getProfesor().getApellidoP()
					+ " " + entity.getProfesor().getApellidoM());
		}

		

		// 👇 NUEVOS CAMPOS DE HORARIO
		dto.setDiaSemana(entity.getDiaSemana());
		dto.setHoraInicio(entity.getHoraInicio());
		dto.setHoraFin(entity.getHoraFin());

		dto.setActivo(entity.getActivo());
		dto.setFechaCreacion(entity.getFechaCreacion());
		dto.setFechaActualizacion(entity.getFechaActualizacion());
		return dto;
	}

	public void updateEntity(Asignacion entity, AsignacionRequestDTO dto, Grupo grupo, Materia materia,
			Profesor profesor) {
		if (entity == null || dto == null)
			return;

		if (grupo != null)
			entity.setGrupo(grupo);
		if (materia != null)
			entity.setMateria(materia);
		if (profesor != null)
			entity.setProfesor(profesor);
		entity.setAula(dto.getAula());

		// 👇 NUEVOS CAMPOS DE HORARIO
		entity.setDiaSemana(dto.getDiaSemana());
		entity.setHoraInicio(dto.getHoraInicio());
		entity.setHoraFin(dto.getHoraFin());

		if (dto.getActivo() != null)
			entity.setActivo(dto.getActivo());
	}
}