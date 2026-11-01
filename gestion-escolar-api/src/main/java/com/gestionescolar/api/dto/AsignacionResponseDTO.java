package com.gestionescolar.api.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.gestionescolar.api.enums.DiaSemana;

public class AsignacionResponseDTO {

	private Long id;

	private Long grupoId;
	private String grupoNombre;
	private Integer grupoGrado;
	private String grupoLetra;

	private Long materiaId;
	private String materiaNombre;
	private String materiaCodigo;

	private Long profesorId;
	private String profesorNombreCompleto;

	private String horario;

	private Boolean activo;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualizacion;

	private DiaSemana diaSemana;
	private LocalTime horaInicio;
	private LocalTime horaFin;

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getMateriaId() {
		return materiaId;
	}

	public void setMateriaId(Long materiaId) {
		this.materiaId = materiaId;
	}

	public String getMateriaNombre() {
		return materiaNombre;
	}

	public void setMateriaNombre(String materiaNombre) {
		this.materiaNombre = materiaNombre;
	}

	public String getMateriaCodigo() {
		return materiaCodigo;
	}

	public void setMateriaCodigo(String materiaCodigo) {
		this.materiaCodigo = materiaCodigo;
	}

	public Long getProfesorId() {
		return profesorId;
	}

	public void setProfesorId(Long profesorId) {
		this.profesorId = profesorId;
	}

	public String getProfesorNombreCompleto() {
		return profesorNombreCompleto;
	}

	public void setProfesorNombreCompleto(String profesorNombreCompleto) {
		this.profesorNombreCompleto = profesorNombreCompleto;
	}



	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
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

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
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