package com.gestionescolar.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public class CalificacionRequestDTO {

	private Long asignacionId;

	private Long alumnoId;

	@DecimalMin(value = "0.0", message = "La calificación no puede ser negativa")
	@DecimalMax(value = "10.0", message = "La calificación no puede exceder 10")
	private BigDecimal calificacion;

	@Size(max = 255)
	private String observaciones;

	private Boolean activo;

	private Integer periodo;

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	// Getters y Setters
	public Long getAsignacionId() {
		return asignacionId;
	}

	public void setAsignacionId(Long asignacionId) {
		this.asignacionId = asignacionId;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public BigDecimal getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(BigDecimal calificacion) {
		this.calificacion = calificacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}