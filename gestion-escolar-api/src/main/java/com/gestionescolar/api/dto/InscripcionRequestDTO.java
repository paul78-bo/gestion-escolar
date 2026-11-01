package com.gestionescolar.api.dto;

import java.time.LocalDate;

import com.gestionescolar.api.enums.EstatusInscripcion;

import jakarta.validation.constraints.*;

public class InscripcionRequestDTO {

	@NotNull(message = "El ID del alumno es obligatorio")
	private Long alumnoId;

	@NotNull(message = "El ID del grupo es obligatorio")
	private Long grupoId;

	private LocalDate fechaInscripcion;

	private EstatusInscripcion estatus;

	private Boolean activo;

	// Getters y Setters
	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public LocalDate getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(LocalDate fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public EstatusInscripcion getEstatus() {
		return estatus;
	}

	public void setEstatus(EstatusInscripcion estatus) {
		this.estatus = estatus;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}