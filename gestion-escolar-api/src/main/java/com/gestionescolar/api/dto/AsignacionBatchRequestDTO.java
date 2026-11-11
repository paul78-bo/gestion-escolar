package com.gestionescolar.api.dto;

import java.time.LocalTime;
import java.util.List;

public class AsignacionBatchRequestDTO {
	private Long grupoId;
	private Long profesorId;
	private List<Long> materiasIds;
	private List<String> diasSemana;
	private LocalTime horaInicio;
	private LocalTime horaFin;

	// Getters y Setters
	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public Long getProfesorId() {
		return profesorId;
	}

	public void setProfesorId(Long profesorId) {
		this.profesorId = profesorId;
	}

	public List<Long> getMateriasIds() {
		return materiasIds;
	}

	public void setMateriasIds(List<Long> materiasIds) {
		this.materiasIds = materiasIds;
	}

	public List<String> getDiasSemana() {
		return diasSemana;
	}

	public void setDiasSemana(List<String> diasSemana) {
		this.diasSemana = diasSemana;
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