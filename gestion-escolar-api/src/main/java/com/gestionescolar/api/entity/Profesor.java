package com.gestionescolar.api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestionescolar.api.enums.EstatusProfesor;
import com.gestionescolar.api.enums.Genero;
import com.gestionescolar.api.enums.GradoAcademico;
import com.gestionescolar.api.enums.TipoContrato;

import jakarta.persistence.*;

@Entity
@Table(name = "profesores")
public class Profesor {

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

	// ❌ QUITADOS: fecha_nacimiento, lugar_nacimiento

	// 🆕 NUEVOS
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

	// ==========================================
	// DATOS PROFESIONALES
	// ==========================================
	@Column(name = "numero_empleado", nullable = false, unique = true, length = 20)
	private String numeroEmpleado;

	@Column(length = 100)
	private String especialidad;

	@Enumerated(EnumType.STRING)
	@Column(name = "grado_academico", length = 30)
	private GradoAcademico gradoAcademico;

	@Column(name = "cedula_profesional", length = 20)
	private String cedulaProfesional;

	@Column(name = "fecha_ingreso")
	private LocalDate fechaIngreso;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_contrato", length = 30)
	private TipoContrato tipoContrato;

	@Column(precision = 10, scale = 2)
	private BigDecimal salario;

	// 🆕 NUEVOS
	@Column(name = "numero_ine", length = 20)
	private String numeroIne;

	@Column(length = 13)
	private String rfc;

	@Column(length = 11)
	private String nss;

	@Column(name = "horario_laboral", length = 100)
	private String horarioLaboral;

	// 🆕 CONTACTO DE EMERGENCIA
	@Column(name = "contacto_emergencia_nombre", length = 150)
	private String contactoEmergenciaNombre;

	@Column(name = "contacto_emergencia_telefono", length = 15)
	private String contactoEmergenciaTelefono;

	@Column(name = "contacto_emergencia_parentesco", length = 50)
	private String contactoEmergenciaParentesco;

	// 🆕 OBSERVACIONES
	@Column(length = 500)
	private String observaciones;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EstatusProfesor estatus = EstatusProfesor.ACTIVO;

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
	// RELACIÓN 1:N CON ASIGNACIONES
	// ==========================================
	@OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Asignacion> asignaciones = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		LocalDateTime ahora = LocalDateTime.now();
		this.fechaCreacion = ahora;
		this.fechaActualizacion = ahora;
		if (this.activo == null)
			this.activo = true;
		if (this.estatus == null)
			this.estatus = EstatusProfesor.ACTIVO;
	}

	@PreUpdate
	public void preUpdate() {
		this.fechaActualizacion = LocalDateTime.now();
	}

	public Profesor() {
	}

	public Profesor(String nombre, String apellidoP, String apellidoM, String email, String numeroEmpleado) {
		this.nombre = nombre;
		this.apellidoP = apellidoP;
		this.apellidoM = apellidoM;
		this.email = email;
		this.numeroEmpleado = numeroEmpleado;
		this.activo = true;
	}

	public void addAsignacion(Asignacion asignacion) {
		if (asignacion != null && !asignaciones.contains(asignacion)) {
			asignaciones.add(asignacion);
			asignacion.setProfesor(this);
		}
	}

	public void removeAsignacion(Asignacion asignacion) {
		if (asignacion != null && asignaciones.remove(asignacion)) {
			asignacion.setProfesor(null);
		}
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

	public String getNumeroEmpleado() {
		return numeroEmpleado;
	}

	public void setNumeroEmpleado(String numeroEmpleado) {
		this.numeroEmpleado = numeroEmpleado;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public GradoAcademico getGradoAcademico() {
		return gradoAcademico;
	}

	public void setGradoAcademico(GradoAcademico gradoAcademico) {
		this.gradoAcademico = gradoAcademico;
	}

	public String getCedulaProfesional() {
		return cedulaProfesional;
	}

	public void setCedulaProfesional(String cedulaProfesional) {
		this.cedulaProfesional = cedulaProfesional;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public TipoContrato getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(TipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public String getNumeroIne() {
		return numeroIne;
	}

	public void setNumeroIne(String numeroIne) {
		this.numeroIne = numeroIne;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public String getHorarioLaboral() {
		return horarioLaboral;
	}

	public void setHorarioLaboral(String horarioLaboral) {
		this.horarioLaboral = horarioLaboral;
	}

	public String getContactoEmergenciaNombre() {
		return contactoEmergenciaNombre;
	}

	public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) {
		this.contactoEmergenciaNombre = contactoEmergenciaNombre;
	}

	public String getContactoEmergenciaTelefono() {
		return contactoEmergenciaTelefono;
	}

	public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) {
		this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
	}

	public String getContactoEmergenciaParentesco() {
		return contactoEmergenciaParentesco;
	}

	public void setContactoEmergenciaParentesco(String contactoEmergenciaParentesco) {
		this.contactoEmergenciaParentesco = contactoEmergenciaParentesco;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public EstatusProfesor getEstatus() {
		return estatus;
	}

	public void setEstatus(EstatusProfesor estatus) {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Asignacion> getAsignaciones() {
		return asignaciones;
	}

	public void setAsignaciones(List<Asignacion> asignaciones) {
		this.asignaciones = asignaciones;
	}
}