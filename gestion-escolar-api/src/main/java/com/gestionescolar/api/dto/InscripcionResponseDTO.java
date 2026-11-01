package com.gestionescolar.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gestionescolar.api.enums.EstatusInscripcion;
import com.gestionescolar.api.enums.Turno;

public class InscripcionResponseDTO {

	private Long id;

	// Alumno
	private Long alumnoId;
	private String alumnoNombreCompleto;
	private String alumnoMatricula;

	// Grupo
	private Long grupoId;
	private String grupoNombre;
	private Integer grupoGrado;
	private String grupoLetra;
	private Turno grupoTurno;

	// Ciclo
	private Long cicloId;
	private String cicloNombre;

	// Inscripción
	private LocalDate fechaInscripcion;
	private EstatusInscripcion estatus;

	// Auditoría
	private Boolean activo;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualizacion;

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public String getGrupoNombre() {
		return grupoNombre;
	}

	public void setGrupoNombre(String grupoNombre) {
		this.grupoNombre = grupoNombre;
	}

	public Integer getGrupoGrado() {
		return grupoGrado;
	}

	public void setGrupoGrado(Integer grupoGrado) {
		this.grupoGrado = grupoGrado;
	}

	public String getGrupoLetra() {
		return grupoLetra;
	}

	public void setGrupoLetra(String grupoLetra) {
		this.grupoLetra = grupoLetra;
	}

	public Turno getGrupoTurno() {
		return grupoTurno;
	}

	public void setGrupoTurno(Turno grupoTurno) {
		this.grupoTurno = grupoTurno;
	}

	public Long getCicloId() {
		return cicloId;
	}

	public void setCicloId(Long cicloId) {
		this.cicloId = cicloId;
	}

	public String getCicloNombre() {
		return cicloNombre;
	}

	public void setCicloNombre(String cicloNombre) {
		this.cicloNombre = cicloNombre;
	}

	public LocalDate getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(LocalDate fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public EstatusInscripcion getEstatus() {
		return estatus;
	}

	public void setEstatus(EstatusInscripcion estatus) {
		this.estatus = estatus;
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