package com.gestionescolar.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class CicloEscolarRequestDTO {

	@NotBlank(message = "El nombre es obligatorio")
	@Pattern(regexp = "\\d{4}-\\d{4}", message = "El formato debe ser YYYY-YYYY (ej: 2026-2027)")
	@Size(max = 20)
	private String nombre;

	@NotNull(message = "La fecha de inicio es obligatoria")
	private LocalDate fechaInicio;

	@NotNull(message = "La fecha de fin es obligatoria")
	private LocalDate fechaFin;

	@NotBlank(message = "El nivel educativo es obligatorio")
	@Size(max = 30)
	private String nivelEducativo;

	@NotBlank(message = "El tipo de periodo es obligatorio")
	@Size(max = 20)
	private String tipoPeriodo;

	@Size(max = 255)
	private String descripcion;

	@Size(max = 500)
	private String observaciones;

	private Boolean activo;

	// Getters y Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getNivelEducativo() {
		return nivelEducativo;
	}

	public void setNivelEducativo(String nivelEducativo) {
		this.nivelEducativo = nivelEducativo;
	}

	public String getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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