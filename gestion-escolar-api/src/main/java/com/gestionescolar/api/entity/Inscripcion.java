package com.gestionescolar.api.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gestionescolar.api.enums.EstatusInscripcion;

import jakarta.persistence.*;

@Entity
@Table(name = "inscripciones", uniqueConstraints = @UniqueConstraint(columnNames = { "alumno_id",
		"grupo_id" }, name = "uk_alumno_grupo"))
public class Inscripcion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alumno_id", nullable = false)
	private Alumno alumno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grupo_id", nullable = false)
	private Grupo grupo;

	@Column(name = "fecha_inscripcion", nullable = false)
	private LocalDate fechaInscripcion;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EstatusInscripcion estatus = EstatusInscripcion.ACTIVA;

	@Column(nullable = false)
	private Boolean activo = true;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_actualizacion")
	private LocalDateTime fechaActualizacion;

	@PrePersist
	public void prePersist() {
		LocalDateTime ahora = LocalDateTime.now();
		this.fechaCreacion = ahora;
		this.fechaActualizacion = ahora;
		if (this.fechaInscripcion == null)
			this.fechaInscripcion = LocalDate.now();
		if (this.activo == null)
			this.activo = true;
		if (this.estatus == null)
			this.estatus = EstatusInscripcion.ACTIVA;
	}

	@PreUpdate
	public void preUpdate() {
		this.fechaActualizacion = LocalDateTime.now();
	}

	public Inscripcion() {
	}

	public Inscripcion(Alumno alumno, Grupo grupo) {
		this.alumno = alumno;
		this.grupo = grupo;
		this.fechaInscripcion = LocalDate.now();
		this.estatus = EstatusInscripcion.ACTIVA;
		this.activo = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
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