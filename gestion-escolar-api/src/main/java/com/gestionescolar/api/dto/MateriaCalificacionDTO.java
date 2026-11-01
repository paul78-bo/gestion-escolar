package com.gestionescolar.api.dto;

import java.math.BigDecimal;

public class MateriaCalificacionDTO {
	private String nombreMateria;
	private String codigoMateria;
	private BigDecimal trimestre1;
	private BigDecimal trimestre2;
	private BigDecimal trimestre3;
	private BigDecimal promedioMateria;

	public MateriaCalificacionDTO() {
	}

	// Getters y Setters
	public String getNombreMateria() {
		return nombreMateria;
	}

	public void setNombreMateria(String nombreMateria) {
		this.nombreMateria = nombreMateria;
	}

	public String getCodigoMateria() {
		return codigoMateria;
	}

	public void setCodigoMateria(String codigoMateria) {
		this.codigoMateria = codigoMateria;
	}

	public BigDecimal getTrimestre1() {
		return trimestre1;
	}

	public void setTrimestre1(BigDecimal trimestre1) {
		this.trimestre1 = trimestre1;
	}

	public BigDecimal getTrimestre2() {
		return trimestre2;
	}

	public void setTrimestre2(BigDecimal trimestre2) {
		this.trimestre2 = trimestre2;
	}

	public BigDecimal getTrimestre3() {
		return trimestre3;
	}

	public void setTrimestre3(BigDecimal trimestre3) {
		this.trimestre3 = trimestre3;
	}

	public BigDecimal getPromedioMateria() {
		return promedioMateria;
	}

	public void setPromedioMateria(BigDecimal promedioMateria) {
		this.promedioMateria = promedioMateria;
	}
}