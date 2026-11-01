package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.ProfesorRequestDTO;
import com.gestionescolar.api.dto.ProfesorResponseDTO;
import com.gestionescolar.api.entity.Profesor;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.enums.EstatusProfesor;
import org.springframework.stereotype.Component;

@Component
public class ProfesorMapper {

	public Profesor toEntity(ProfesorRequestDTO dto, Usuario usuario) {
		if (dto == null)
			return null;

		Profesor p = new Profesor();

		// Datos personales
		p.setNombre(dto.getNombre());
		p.setApellidoP(dto.getApellidoP());
		p.setApellidoM(dto.getApellidoM());
		p.setCurp(dto.getCurp());
		p.setEmail(dto.getEmail());
		p.setTelefono(dto.getTelefono());
		p.setDireccion(dto.getDireccion());
		p.setGenero(dto.getGenero());

		// 🆕 Campos nuevos personales
		p.setEstadoCivil(dto.getEstadoCivil());
		p.setNacionalidad(dto.getNacionalidad());
		p.setPaisNacimiento(dto.getPaisNacimiento());
		p.setEntidadNacimiento(dto.getEntidadNacimiento());
		p.setMunicipio(dto.getMunicipio());
		p.setColonia(dto.getColonia());
		p.setCodigoPostal(dto.getCodigoPostal());
		p.setCalle(dto.getCalle());
		p.setNumeroExterior(dto.getNumeroExterior());
		p.setNumeroInterior(dto.getNumeroInterior());

		// Datos profesionales
		// ⚠️ numeroEmpleado se autogenera en el ServiceImpl, NO lo seteamos aquí
		p.setEspecialidad(dto.getEspecialidad());
		p.setGradoAcademico(dto.getGradoAcademico());
		p.setCedulaProfesional(dto.getCedulaProfesional());
		p.setFechaIngreso(dto.getFechaIngreso());
		p.setTipoContrato(dto.getTipoContrato());
		p.setSalario(dto.getSalario());

		// 🆕 Campos nuevos profesionales
		p.setNumeroIne(dto.getNumeroIne());
		p.setRfc(dto.getRfc());
		p.setNss(dto.getNss());
		p.setHorarioLaboral(dto.getHorarioLaboral());

		// 🆕 Contacto de emergencia
		p.setContactoEmergenciaNombre(dto.getContactoEmergenciaNombre());
		p.setContactoEmergenciaTelefono(dto.getContactoEmergenciaTelefono());
		p.setContactoEmergenciaParentesco(dto.getContactoEmergenciaParentesco());

		// 🆕 Observaciones
		p.setObservaciones(dto.getObservaciones());

		// Estatus y auditoría
		p.setEstatus(dto.getEstatus() != null ? dto.getEstatus() : EstatusProfesor.ACTIVO);
		p.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
		p.setUsuario(usuario);

		return p;
	}

	public ProfesorResponseDTO toResponseDTO(Profesor entity) {
		if (entity == null)
			return null;

		ProfesorResponseDTO dto = new ProfesorResponseDTO();
		dto.setId(entity.getId());

		// Datos personales
		dto.setNombre(entity.getNombre());
		dto.setApellidoP(entity.getApellidoP());
		dto.setApellidoM(entity.getApellidoM());
		dto.setCurp(entity.getCurp());
		dto.setEmail(entity.getEmail());
		dto.setTelefono(entity.getTelefono());
		dto.setDireccion(entity.getDireccion());
		dto.setGenero(entity.getGenero());

		// 🆕
		dto.setEstadoCivil(entity.getEstadoCivil());
		dto.setNacionalidad(entity.getNacionalidad());
		dto.setPaisNacimiento(entity.getPaisNacimiento());
		dto.setEntidadNacimiento(entity.getEntidadNacimiento());
		dto.setMunicipio(entity.getMunicipio());
		dto.setColonia(entity.getColonia());
		dto.setCodigoPostal(entity.getCodigoPostal());
		dto.setCalle(entity.getCalle());
		dto.setNumeroExterior(entity.getNumeroExterior());
		dto.setNumeroInterior(entity.getNumeroInterior());

		// Datos profesionales
		dto.setNumeroEmpleado(entity.getNumeroEmpleado());
		dto.setEspecialidad(entity.getEspecialidad());
		dto.setGradoAcademico(entity.getGradoAcademico());
		dto.setCedulaProfesional(entity.getCedulaProfesional());
		dto.setFechaIngreso(entity.getFechaIngreso());
		dto.setTipoContrato(entity.getTipoContrato());
		dto.setSalario(entity.getSalario());

		// 🆕
		dto.setNumeroIne(entity.getNumeroIne());
		dto.setRfc(entity.getRfc());
		dto.setNss(entity.getNss());
		dto.setHorarioLaboral(entity.getHorarioLaboral());

		// 🆕 Emergencia
		dto.setContactoEmergenciaNombre(entity.getContactoEmergenciaNombre());
		dto.setContactoEmergenciaTelefono(entity.getContactoEmergenciaTelefono());
		dto.setContactoEmergenciaParentesco(entity.getContactoEmergenciaParentesco());

		// 🆕 Observaciones
		dto.setObservaciones(entity.getObservaciones());

		dto.setEstatus(entity.getEstatus());

		// Auditoría
		dto.setActivo(entity.getActivo());
		dto.setFechaCreacion(entity.getFechaCreacion());
		dto.setFechaActualizacion(entity.getFechaActualizacion());

		if (entity.getUsuario() != null) {
			dto.setUsuarioId(entity.getUsuario().getId());
			dto.setUsername(entity.getUsuario().getUsername());
		}

		return dto;
	}

	public void updateEntity(Profesor entity, ProfesorRequestDTO dto) {
		if (entity == null || dto == null)
			return;

		// Datos personales
		entity.setNombre(dto.getNombre());
		entity.setApellidoP(dto.getApellidoP());
		entity.setApellidoM(dto.getApellidoM());
		entity.setCurp(dto.getCurp());
		entity.setEmail(dto.getEmail());
		entity.setTelefono(dto.getTelefono());
		entity.setDireccion(dto.getDireccion());
		entity.setGenero(dto.getGenero());

		// 🆕
		entity.setEstadoCivil(dto.getEstadoCivil());
		entity.setNacionalidad(dto.getNacionalidad());
		entity.setPaisNacimiento(dto.getPaisNacimiento());
		entity.setEntidadNacimiento(dto.getEntidadNacimiento());
		entity.setMunicipio(dto.getMunicipio());
		entity.setColonia(dto.getColonia());
		entity.setCodigoPostal(dto.getCodigoPostal());
		entity.setCalle(dto.getCalle());
		entity.setNumeroExterior(dto.getNumeroExterior());
		entity.setNumeroInterior(dto.getNumeroInterior());

		// Datos profesionales
		// ⚠️ numeroEmpleado NO se actualiza (es inmutable una vez creado)
		entity.setEspecialidad(dto.getEspecialidad());
		entity.setGradoAcademico(dto.getGradoAcademico());
		entity.setCedulaProfesional(dto.getCedulaProfesional());
		entity.setFechaIngreso(dto.getFechaIngreso());
		entity.setTipoContrato(dto.getTipoContrato());
		entity.setSalario(dto.getSalario());

		// 🆕
		entity.setNumeroIne(dto.getNumeroIne());
		entity.setRfc(dto.getRfc());
		entity.setNss(dto.getNss());
		entity.setHorarioLaboral(dto.getHorarioLaboral());

		// 🆕 Emergencia
		entity.setContactoEmergenciaNombre(dto.getContactoEmergenciaNombre());
		entity.setContactoEmergenciaTelefono(dto.getContactoEmergenciaTelefono());
		entity.setContactoEmergenciaParentesco(dto.getContactoEmergenciaParentesco());

		// 🆕 Observaciones
		entity.setObservaciones(dto.getObservaciones());

		if (dto.getEstatus() != null)
			entity.setEstatus(dto.getEstatus());
		if (dto.getActivo() != null)
			entity.setActivo(dto.getActivo());
	}
}