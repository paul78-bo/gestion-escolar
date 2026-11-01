package com.gestionescolar.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class BoletaResponseDTO {
	private Long alumnoId;
	private String matricula;
	private String nombreCompleto;
	private String grupoNombre;
	private String cicloNombre;
	private List<CampoFormativoDTO> camposFormativos;
	private BigDecimal promedioFinal;

	private BigDecimal promedioT1;
	private BigDecimal promedioT2;
	private BigDecimal promedioT3;

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getGrupoNombre() {
		return grupoNombre;
	}

	public void setGrupoNombre(String grupoNombre) {
		this.grupoNombre = grupoNombre;
	}

	public String getCicloNombre() {
		return cicloNombre;
	}

	public void setCicloNombre(String cicloNombre) {
		this.cicloNombre = cicloNombre;
	}

	public List<CampoFormativoDTO> getCamposFormativos() {
		return camposFormativos;
	}

	public void setCamposFormativos(List<CampoFormativoDTO> camposFormativos) {
		this.camposFormativos = camposFormativos;
	}

	public BigDecimal getPromedioFinal() {
		return promedioFinal;
	}

	public void setPromedioFinal(BigDecimal promedioFinal) {
		this.promedioFinal = promedioFinal;
	}

	public BigDecimal getPromedioT1() {
		return promedioT1;
	}

	public void setPromedioT1(BigDecimal promedioT1) {
		this.promedioT1 = promedioT1;
	}

	public BigDecimal getPromedioT2() {
		return promedioT2;
	}

	public void setPromedioT2(BigDecimal promedioT2) {
		this.promedioT2 = promedioT2;
	}

	public BigDecimal getPromedioT3() {
		return promedioT3;
	}

	public void setPromedioT3(BigDecimal promedioT3) {
		this.promedioT3 = promedioT3;
	}
	
}