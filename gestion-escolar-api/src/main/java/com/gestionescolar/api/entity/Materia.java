package com.gestionescolar.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestionescolar.api.enums.CampoFormativo;

import jakarta.persistence.*;

@Entity
@Table(name = "materias")
public class Materia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@Column(name = "creditos") // Quita el nullable = false
	private Integer creditos;
	
	@Column(name = "horas_semana")
	private Integer horasSemana;

	@Enumerated(EnumType.STRING)
	@Column(name = "campo_formativo", length = 50)
	private CampoFormativo campoFormativo;

	@Column(length = 500)
	private String descripcion;

	@Column(nullable = false)
	private Boolean activo = true;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_actualizacion")
	private LocalDateTime fechaActualizacion;

	// ==========================================
	// RELACIÓN 1:N CON ASIGNACIONES
	// ==========================================
	@OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Asignacion> asignaciones = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		LocalDateTime ahora = LocalDateTime.now();
		this.fechaCreacion = ahora;
		this.fechaActualizacion = ahora;
		if (this.activo == null)
			this.activo = true;
	}

	@PreUpdate
	public void preUpdate() {
		this.fechaActualizacion = LocalDateTime.now();
	}

	public Materia() {
	}

	public Materia(String nombre, String codigo, Integer creditos) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.creditos = creditos;
		this.activo = true;
	}

	public void addAsignacion(Asignacion asignacion) {
		if (asignacion != null && !asignaciones.contains(asignacion)) {
			asignaciones.add(asignacion);
			asignacion.setMateria(this);
		}
	}

	public void removeAsignacion(Asignacion asignacion) {
		if (asignacion != null && asignaciones.remove(asignacion)) {
			asignacion.setMateria(null);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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



	public CampoFormativo getCampoFormativo() {
		return campoFormativo;
	}

	public void setCampoFormativo(CampoFormativo campoFormativo) {
		this.campoFormativo = campoFormativo;
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

	public List<Asignacion> getAsignaciones() {
		return asignaciones;
	}

	public void setAsignaciones(List<Asignacion> asignaciones) {
		this.asignaciones = asignaciones;
	}
}