package com.gestionescolar.api.mapper;

import com.gestionescolar.api.dto.AlumnoRequestDTO;
import com.gestionescolar.api.dto.AlumnoResponseDTO;
import com.gestionescolar.api.entity.Alumno;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.enums.EstatusAlumno;
import org.springframework.stereotype.Component;

@Component
public class AlumnoMapper {

	public Alumno toEntity(AlumnoRequestDTO dto, Usuario usuario) {
		if (dto == null)
			return null;

		Alumno alumno = new Alumno();
		// Personales
		alumno.setNombre(dto.getNombre());
		alumno.setApellidoP(dto.getApellidoP());
		alumno.setApellidoM(dto.getApellidoM());
		alumno.setCurp(dto.getCurp());
		alumno.setEmail(dto.getEmail());
		alumno.setTelefono(dto.getTelefono());
		alumno.setGenero(dto.getGenero());
		alumno.setFechaNacimiento(dto.getFechaNacimiento());
		alumno.setEstadoCivil(dto.getEstadoCivil());
		alumno.setNacionalidad(dto.getNacionalidad());
		alumno.setObservaciones(dto.getObservaciones());

		// Nacimiento
		alumno.setPaisNacimiento(dto.getPaisNacimiento());
		alumno.setEntidadNacimiento(dto.getEntidadNacimiento());
		alumno.setMunicipioNacimiento(dto.getMunicipioNacimiento());
		alumno.setLocalidadNacimiento(dto.getLocalidadNacimiento());

		// Domicilio
		alumno.setCalle(dto.getCalle());
		alumno.setNumeroExterior(dto.getNumeroExterior());
		alumno.setNumeroInterior(dto.getNumeroInterior());
		alumno.setColonia(dto.getColonia());
		alumno.setCodigoPostal(dto.getCodigoPostal());
		alumno.setLocalidad(dto.getLocalidad());
		alumno.setMunicipio(dto.getMunicipio());
		alumno.setEntidadFederativa(dto.getEntidadFederativa());

		// Académicos
		alumno.setMatricula(dto.getMatricula());
		alumno.setEstatus(dto.getEstatus() != null ? dto.getEstatus() : EstatusAlumno.ACTIVO);
		alumno.setFechaIngreso(dto.getFechaIngreso());

		// Tutor
		alumno.setTutorNombre(dto.getTutorNombre());
		alumno.setTutorTelefono(dto.getTutorTelefono());
		alumno.setTutorEmail(dto.getTutorEmail());

		alumno.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
		alumno.setUsuario(usuario);

		return alumno;
	}

	public AlumnoResponseDTO toResponseDTO(Alumno entity) {
		if (entity == null)
			return null;

		AlumnoResponseDTO dto = new AlumnoResponseDTO();
		dto.setId(entity.getId());

		// Personales
		dto.setNombre(entity.getNombre());
		dto.setApellidoP(entity.getApellidoP());
		dto.setApellidoM(entity.getApellidoM());
		dto.setCurp(entity.getCurp());
		dto.setEmail(entity.getEmail());
		dto.setTelefono(entity.getTelefono());
		dto.setGenero(entity.getGenero());
		dto.setFechaNacimiento(entity.getFechaNacimiento());
		dto.setEstadoCivil(entity.getEstadoCivil());
		dto.setNacionalidad(entity.getNacionalidad());
		dto.setObservaciones(entity.getObservaciones());

		// Nacimiento
		dto.setPaisNacimiento(entity.getPaisNacimiento());
		dto.setEntidadNacimiento(entity.getEntidadNacimiento());
		dto.setMunicipioNacimiento(entity.getMunicipioNacimiento());
		dto.setLocalidadNacimiento(entity.getLocalidadNacimiento());

		// Domicilio
		dto.setCalle(entity.getCalle());
		dto.setNumeroExterior(entity.getNumeroExterior());
		dto.setNumeroInterior(entity.getNumeroInterior());
		dto.setColonia(entity.getColonia());
		dto.setCodigoPostal(entity.getCodigoPostal());
		dto.setLocalidad(entity.getLocalidad());
		dto.setMunicipio(entity.getMunicipio());
		dto.setEntidadFederativa(entity.getEntidadFederativa());

		// Académicos
		dto.setMatricula(entity.getMatricula());
		dto.setEstatus(entity.getEstatus());
		dto.setFechaIngreso(entity.getFechaIngreso());

		// Tutor
		dto.setTutorNombre(entity.getTutorNombre());
		dto.setTutorTelefono(entity.getTutorTelefono());
		dto.setTutorEmail(entity.getTutorEmail());

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

	public void updateEntity(Alumno entity, AlumnoRequestDTO dto, Usuario usuario) {
		if (entity == null || dto == null)
			return;

		// Personales
		entity.setNombre(dto.getNombre());
		entity.setApellidoP(dto.getApellidoP());
		entity.setApellidoM(dto.getApellidoM());
		entity.setCurp(dto.getCurp());
		entity.setEmail(dto.getEmail());
		entity.setTelefono(dto.getTelefono());
		entity.setGenero(dto.getGenero());
		entity.setFechaNacimiento(dto.getFechaNacimiento());
		entity.setEstadoCivil(dto.getEstadoCivil());
		entity.setNacionalidad(dto.getNacionalidad());
		entity.setObservaciones(dto.getObservaciones());

		// Nacimiento
		entity.setPaisNacimiento(dto.getPaisNacimiento());
		entity.setEntidadNacimiento(dto.getEntidadNacimiento());
		entity.setMunicipioNacimiento(dto.getMunicipioNacimiento());
		entity.setLocalidadNacimiento(dto.getLocalidadNacimiento());

		// Domicilio
		entity.setCalle(dto.getCalle());
		entity.setNumeroExterior(dto.getNumeroExterior());
		entity.setNumeroInterior(dto.getNumeroInterior());
		entity.setColonia(dto.getColonia());
		entity.setCodigoPostal(dto.getCodigoPostal());
		entity.setLocalidad(dto.getLocalidad());
		entity.setMunicipio(dto.getMunicipio());
		entity.setEntidadFederativa(dto.getEntidadFederativa());

		// Académicos
		entity.setMatricula(dto.getMatricula());
		if (dto.getEstatus() != null)
			entity.setEstatus(dto.getEstatus());
		entity.setFechaIngreso(dto.getFechaIngreso());

		// Tutor
		entity.setTutorNombre(dto.getTutorNombre());
		entity.setTutorTelefono(dto.getTutorTelefono());
		entity.setTutorEmail(dto.getTutorEmail());

		// Auditoría
		if (dto.getActivo() != null)
			entity.setActivo(dto.getActivo());
		if (usuario != null)
			entity.setUsuario(usuario);
	}
}