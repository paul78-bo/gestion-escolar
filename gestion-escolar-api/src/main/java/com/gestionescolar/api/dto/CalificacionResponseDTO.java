package com.gestionescolar.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CalificacionResponseDTO {

	private Long id;

	private Long asignacionId;
	private String materiaNombre;
	private String grupoNombre;

	private Long alumnoId;
	private String alumnoNombreCompleto;
	private String alumnoMatricula;

	private BigDecimal calificacion;
	private String observaciones;

	private Boolean activo;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualizacion;
	
	private Integer periodo;
	
	

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAsignacionId() {
		return asignacionId;
	}

	public void setAsignacionId(Long asignacionId) {
		this.asignacionId = asignacionId;
	}

	public String getMateriaNombre() {
		return materiaNombre;
	}

	public void setMateriaNombre(String materiaNombre) {
		this.materiaNombre = materiaNombre;
	}

	public String getGrupoNombre() {
		return grupoNombre;
	}

	public void setGrupoNombre(String grupoNombre) {
		this.grupoNombre = grupoNombre;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public String getAlumnoNombreCompleto() {
		return alumnoNombreCompleto;
	}

	public void setAlumnoNombreCompleto(String alumnoNombreCompleto) {
		this.alumnoNombreCompleto = alumnoNombreCompleto;
	}

	public String getAlumnoMatricula() {
		return alumnoMatricula;
	}

	public void setAlumnoMatricula(String alumnoMatricula) {
		this.alumnoMatricula = alumnoMatricula;
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

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
}