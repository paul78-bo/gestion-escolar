package com.gestionescolar.api.dto;

import com.gestionescolar.api.enums.Turno;

import jakarta.validation.constraints.*;

public class GrupoRequestDTO {

	@NotNull(message = "El grado es obligatorio")
	@Min(value = 1, message = "El grado debe ser al menos 1")
	@Max(value = 12, message = "El grado no puede exceder 12")
	private Integer grado;

	@NotBlank(message = "La letra del grupo es obligatoria")
	@Size(max = 5)
	private String letra;

	private Turno turno;

	@Min(value = 1)
	@Max(value = 100)
	private Integer capacidad;

	@NotNull(message = "El ciclo escolar es obligatorio")
	private Long cicloId;

	private Boolean activo;

	// Getters y Setters
	public Integer getGrado() {
		return grado;
	}

	public void setGrado(Integer grado) {
		this.grado = grado;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public Long getCicloId() {
		return cicloId;
	}

	public void setCicloId(Long cicloId) {
		this.cicloId = cicloId;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}