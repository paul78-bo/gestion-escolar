package com.gestionescolar.api.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestionescolar.api.enums.EstatusAlumno;
import com.gestionescolar.api.enums.Genero;

import jakarta.persistence.*;

@Entity
@Table(name = "alumnos")
public class Alumno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ==========================================
	// DATOS PERSONALES
	// ==========================================
	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String apellidoP;

	@Column(nullable = false, length = 100)
	private String apellidoM;

	@Column(unique = true, length = 18)
	private String curp;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(length = 15)
	private String telefono;

	@Column(length = 255)
	private String direccion;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Genero genero;

	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;

	@Column(name = "lugar_nacimiento", length = 100)
	private String lugarNacimiento;

	// 🆕 CAMPOS NUEVOS
	@Column(name = "estado_civil", length = 20)
	private String estadoCivil;

	@Column(length = 50)
	private String nacionalidad;

	@Column(name = "pais_nacimiento", length = 50)
	private String paisNacimiento;

	@Column(name = "entidad_nacimiento", length = 50)
	private String entidadNacimiento;

	@Column(length = 50)
	private String municipio;

	@Column(length = 100)
	private String colonia;

	@Column(name = "codigo_postal", length = 10)
	private String codigoPostal;

	@Column(length = 100)
	private String calle;

	@Column(name = "numero_exterior", length = 10)
	private String numeroExterior;

	@Column(name = "numero_interior", length = 10)
	private String numeroInterior;

	@Column(length = 500)
	private String observaciones;

	// ==========================================
	// DATOS ACADÉMICOS
	// ==========================================
	@Column(nullable = false, unique = true, length = 20)
	private String matricula;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EstatusAlumno estatus = EstatusAlumno.ACTIVO;

	@Column(name = "fecha_ingreso")
	private LocalDate fechaIngreso;

	// ==========================================
	// DATOS DEL TUTOR
	// ==========================================
	@Column(name = "tutor_nombre", length = 150)
	private String tutorNombre;

	@Column(name = "tutor_telefono", length = 15)
	private String tutorTelefono;

	@Column(name = "tutor_email", length = 150)
	private String tutorEmail;

	// ==========================================
	// AUDITORÍA
	// ==========================================
	@Column(nullable = false)
	private Boolean activo = true;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_actualizacion")
	private LocalDateTime fechaActualizacion;

	// ==========================================
	// RELACIÓN 1:1 CON USUARIO
	// ==========================================
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", unique = true)
	private Usuario usuario;

	// ==========================================
	// RELACIÓN 1:N CON INSCRIPCIONES
	// ==========================================
	@OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Inscripcion> inscripciones = new ArrayList<>();

	// ==========================================
	// RELACIÓN 1:N CON CALIFICACIONES
	// ==========================================
	@OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Calificacion> calificaciones = new ArrayList<>();

	// ==========================================
	// MÉTODOS HELPER
	// ==========================================
	public void addInscripcion(Inscripcion inscripcion) {
		if (inscripcion != null && !inscripciones.contains(inscripcion)) {
			inscripciones.add(inscripcion);
			inscripcion.setAlumno(this);
		}
	}

	public void removeInscripcion(Inscripcion inscripcion) {
		if (inscripcion != null && inscripciones.remove(inscripcion)) {
			inscripcion.setAlumno(null);
		}
	}

	public void addCalificacion(Calificacion calificacion) {
		if (calificacion != null && !calificaciones.contains(calificacion)) {
			calificaciones.add(calificacion);
			calificacion.setAlumno(this);
		}
	}

	public void removeCalificacion(Calificacion calificacion) {
		if (calificacion != null && calificaciones.remove(calificacion)) {
			calificacion.setAlumno(null);
		}
	}

	// ==========================================
	// JPA
	// ==========================================
	@PrePersist
	public void prePersist() {
		LocalDateTime ahora = LocalDateTime.now();
		this.fechaCreacion = ahora;
		this.fechaActualizacion = ahora;
		if (this.activo == null)
			this.activo = true;
		if (this.estatus == null)
			this.estatus = EstatusAlumno.ACTIVO;
	}

	@PreUpdate
	public void preUpdate() {
		this.fechaActualizacion = LocalDateTime.now();
	}

	// ==========================================
	// CONSTRUCTORES
	// ==========================================
	public Alumno() {
	}

	public Alumno(String nombre, String apellidoP, String apellidoM, String email, String matricula) {
		this.nombre = nombre;
		this.apellidoP = apellidoP;
		this.apellidoM = apellidoM;
		this.email = email;
		this.matricula = matricula;
		this.activo = true;
		this.estatus = EstatusAlumno.ACTIVO;
	}

	// ==========================================
	// GETTERS Y SETTERS
	// ==========================================
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

	public String getApellidoP() {
		return apellidoP;
	}

	public void setApellidoP(String apellidoP) {
		this.apellidoP = apellidoP;
	}

	public String getApellidoM() {
		return apellidoM;
	}

	public void setApellidoM(String apellidoM) {
		this.apellidoM = apellidoM;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	// 🆕 GETTERS Y SETTERS NUEVOS
	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getPaisNacimiento() {
		return paisNacimiento;
	}

	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}

	public String getEntidadNacimiento() {
		return entidadNacimiento;
	}

	public void setEntidadNacimiento(String entidadNacimiento) {
		this.entidadNacimiento = entidadNacimiento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumeroExterior() {
		return numeroExterior;
	}

	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}

	public String getNumeroInterior() {
		return numeroInterior;
	}

	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public EstatusAlumno getEstatus() {
		return estatus;
	}

	public void setEstatus(EstatusAlumno estatus) {
		this.estatus = estatus;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getTutorNombre() {
		return tutorNombre;
	}

	public void setTutorNombre(String tutorNombre) {
		this.tutorNombre = tutorNombre;
	}

	public String getTutorTelefono() {
		return tutorTelefono;
	}

	public void setTutorTelefono(String tutorTelefono) {
		this.tutorTelefono = tutorTelefono;
	}

	public String getTutorEmail() {
		return tutorEmail;
	}

	public void setTutorEmail(String tutorEmail) {
		this.tutorEmail = tutorEmail;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Inscripcion> getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(List<Inscripcion> inscripciones) {
		this.inscripciones = inscripciones;
	}

	public List<Calificacion> getCalificaciones() {
		return calificaciones;
	}

	public void setCalificaciones(List<Calificacion> calificaciones) {
		this.calificaciones = calificaciones;
	}
}