package com.gestionescolar.api.dto;

import java.time.LocalTime;

import com.gestionescolar.api.enums.DiaSemana;

import jakarta.validation.constraints.*;

public class AsignacionRequestDTO {

	@NotNull(message = "El grupo es obligatorio")
	private Long grupoId;

	@NotNull(message = "La materia es obligatoria")
	private Long materiaId;

	@NotNull(message = "El profesor es obligatorio")
	private Long profesorId;

	@Size(max = 20)
	private String aula;

	private DiaSemana diaSemana;
	private LocalTime horaInicio;
	private LocalTime horaFin;

	private Boolean activo;

	// Getters y Setters
	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public Long getMateriaId() {
		return materiaId;
	}

	public void setMateriaId(Long materiaId) {
		this.materiaId = materiaId;
	}

	public Long getProfesorId() {
		return profesorId;
	}

	public void setProfesorId(Long profesorId) {
		this.profesorId = profesorId;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}
	
	
	
}