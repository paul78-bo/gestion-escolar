package com.gestionescolar.api.dto;

public class DashboardResponseDTO {

	private Long totalAlumnos;
	private Long totalProfesores;
	private Long totalMaterias;
	private Long totalGrupos;
	private Long totalCiclos;
	private Long totalAsignaciones;
	private Long totalInscripciones;
	private Long totalCalificaciones;

	private Long alumnosActivos;
	private Long profesoresActivos;
	private Long materiasActivas;
	private Long gruposActivos;
	private Long ciclosActivos;

	public DashboardResponseDTO() {
	}

	public Long getTotalAlumnos() {
		return totalAlumnos;
	}

	public void setTotalAlumnos(Long totalAlumnos) {
		this.totalAlumnos = totalAlumnos;
	}

	public Long getTotalProfesores() {
		return totalProfesores;
	}

	public void setTotalProfesores(Long totalProfesores) {
		this.totalProfesores = totalProfesores;
	}

	public Long getTotalMaterias() {
		return totalMaterias;
	}

	public void setTotalMaterias(Long totalMaterias) {
		this.totalMaterias = totalMaterias;
	}

	public Long getTotalGrupos() {
		return totalGrupos;
	}

	public void setTotalGrupos(Long totalGrupos) {
		this.totalGrupos = totalGrupos;
	}

	public Long getTotalCiclos() {
		return totalCiclos;
	}

	public void setTotalCiclos(Long totalCiclos) {
		this.totalCiclos = totalCiclos;
	}

	public Long getTotalAsignaciones() {
		return totalAsignaciones;
	}

	public void setTotalAsignaciones(Long totalAsignaciones) {
		this.totalAsignaciones = totalAsignaciones;
	}

	public Long getTotalInscripciones() {
		return totalInscripciones;
	}

	public void setTotalInscripciones(Long totalInscripciones) {
		this.totalInscripciones = totalInscripciones;
	}

	public Long getTotalCalificaciones() {
		return totalCalificaciones;
	}

	public void setTotalCalificaciones(Long totalCalificaciones) {
		this.totalCalificaciones = totalCalificaciones;
	}

	public Long getAlumnosActivos() {
		return alumnosActivos;
	}

	public void setAlumnosActivos(Long alumnosActivos) {
		this.alumnosActivos = alumnosActivos;
	}

	public Long getProfesoresActivos() {
		return profesoresActivos;
	}

	public void setProfesoresActivos(Long profesoresActivos) {
		this.profesoresActivos = profesoresActivos;
	}

	public Long getMateriasActivas() {
		return materiasActivas;
	}

	public void setMateriasActivas(Long materiasActivas) {
		this.materiasActivas = materiasActivas;
	}

	public Long getGruposActivos() {
		return gruposActivos;
	}

	public void setGruposActivos(Long gruposActivos) {
		this.gruposActivos = gruposActivos;
	}

	public Long getCiclosActivos() {
		return ciclosActivos;
	}

	public void setCiclosActivos(Long ciclosActivos) {
		this.ciclosActivos = ciclosActivos;
	}
}