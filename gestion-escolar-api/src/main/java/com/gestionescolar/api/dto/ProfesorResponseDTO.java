package com.gestionescolar.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gestionescolar.api.enums.EstatusProfesor;
import com.gestionescolar.api.enums.Genero;
import com.gestionescolar.api.enums.GradoAcademico;
import com.gestionescolar.api.enums.TipoContrato;

public class ProfesorResponseDTO {

	private Long id;

	// Datos personales
	private String nombre;
	private String apellidoP;
	private String apellidoM;
	private String curp;
	private String email;
	private String telefono;
	private String direccion;
	private Genero genero;

	// 🆕
	private String estadoCivil;
	private String nacionalidad;
	private String paisNacimiento;
	private String entidadNacimiento;
	private String municipio;
	private String colonia;
	private String codigoPostal;
	private String calle;
	private String numeroExterior;
	private String numeroInterior;

	// Datos profesionales
	private String numeroEmpleado;
	private String especialidad;
	private GradoAcademico gradoAcademico;
	private String cedulaProfesional;
	private LocalDate fechaIngreso;
	private TipoContrato tipoContrato;
	private BigDecimal salario;

	// 🆕
	private String numeroIne;
	private String rfc;
	private String nss;
	private String horarioLaboral;

	// 🆕 Emergencia
	private String contactoEmergenciaNombre;
	private String contactoEmergenciaTelefono;
	private String contactoEmergenciaParentesco;

	// 🆕 Observaciones
	private String observaciones;

	private EstatusProfesor estatus;

	// Auditoría
	private Boolean activo;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualizacion;

	private Long usuarioId;
	private String username;

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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}