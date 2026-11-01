package com.gestionescolar.api.dto;


import com.gestionescolar.api.enums.CampoFormativo;

import jakarta.validation.constraints.*;

public class MateriaRequestDTO {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100)
	private String nombre;

	@NotBlank(message = "El código es obligatorio")
	@Size(max = 20)
	private String codigo;

//	@NotNull(message = "Los créditos son obligatorios")
//	@Min(value = 1, message = "Los créditos deben ser al menos 1")
//	@Max(value = 20, message = "Los créditos no pueden exceder 20")
	private Integer creditos;

//	@Max(value = 40)
	private Integer horasSemana;

	
	private CampoFormativo campo;

	@Size(max = 500)
	private String descripcion;

	private Boolean activo;

	// Getters y Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getCreditos() {
		return creditos;
	}

	public void setCreditos(Integer creditos) {
		this.creditos = creditos;
	}

	public Integer getHorasSemana() {
		return horasSemana;
	}

	public void setHorasSemana(Integer horasSemana) {
		this.horasSemana = horasSemana;
	}



	public CampoFormativo getCampo() {
		return campo;
	}

	public void setCampo(CampoFormativo campo) {
		this.campo = campo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}