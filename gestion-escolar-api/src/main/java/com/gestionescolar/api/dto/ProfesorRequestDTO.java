package com.gestionescolar.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gestionescolar.api.enums.EstatusProfesor;
import com.gestionescolar.api.enums.Genero;
import com.gestionescolar.api.enums.GradoAcademico;
import com.gestionescolar.api.enums.TipoContrato;

import jakarta.validation.constraints.*;

public class ProfesorRequestDTO {

	// ==========================================
	// DATOS PERSONALES
	// ==========================================
	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100)
	private String nombre;

	@NotBlank(message = "El apellido paterno es obligatorio")
	@Size(max = 100)
	private String apellidoP;

	@NotBlank(message = "El apellido materno es obligatorio")
	@Size(max = 100)
	private String apellidoM;

	@NotBlank(message = "La CURP es obligatoria")
	@Size(min = 18, max = 18, message = "La CURP debe tener 18 caracteres")
	private String curp;

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email debe ser válido")
	@Size(max = 150)
	private String email;

	@NotBlank(message = "El teléfono es obligatorio")
	@Size(max = 15)
	private String telefono;

	@NotBlank(message = "La dirección es obligatoria")
	@Size(max = 255)
	private String direccion;

	@NotNull(message = "El género es obligatorio")
	private Genero genero;

	// 🆕 CAMPOS NUEVOS
	@NotBlank(message = "El estado civil es obligatorio")
	@Size(max = 20)
	private String estadoCivil;

	@NotBlank(message = "La nacionalidad es obligatoria")
	@Size(max = 50)
	private String nacionalidad;

	@NotBlank(message = "El país de nacimiento es obligatorio")
	@Size(max = 50)
	private String paisNacimiento;

	@NotBlank(message = "La entidad de nacimiento es obligatoria")
	@Size(max = 50)
	private String entidadNacimiento;

	@NotBlank(message = "El municipio es obligatorio")
	@Size(max = 50)
	private String municipio;

	@NotBlank(message = "La colonia es obligatoria")
	@Size(max = 100)
	private String colonia;

	@NotBlank(message = "El código postal es obligatorio")
	@Size(max = 10)
	private String codigoPostal;

	@NotBlank(message = "La calle es obligatoria")
	@Size(max = 100)
	private String calle;

	@NotBlank(message = "El número exterior es obligatorio")
	@Size(max = 10)
	private String numeroExterior;

	@Size(max = 10)
	private String numeroInterior;

	// ==========================================
	// DATOS PROFESIONALES
	// ==========================================
	// ⚠️ numeroEmpleado NO se pide al crear (se autogenera)
	// Solo se puede enviar al editar (opcional, no editable realmente)
	@Size(max = 20)
	private String numeroEmpleado;

	@NotBlank(message = "La especialidad es obligatoria")
	@Size(max = 100)
	private String especialidad;

	@NotNull(message = "El grado académico es obligatorio")
	private GradoAcademico gradoAcademico;

	@NotBlank(message = "La cédula profesional es obligatoria")
	@Size(max = 20)
	private String cedulaProfesional;

	@NotNull(message = "La fecha de ingreso es obligatoria")
	private LocalDate fechaIngreso;

	@NotNull(message = "El tipo de contrato es obligatorio")
	private TipoContrato tipoContrato;

	@NotNull(message = "El salario es obligatorio")
	@DecimalMin(value = "0.0", message = "El salario no puede ser negativo")
	private BigDecimal salario;

	@NotBlank(message = "El número de INE es obligatorio")
	@Size(max = 20)
	private String numeroIne;

	@NotBlank(message = "El RFC es obligatorio")
	@Size(min = 12, max = 13, message = "El RFC debe tener 12 o 13 caracteres")
	private String rfc;

	@NotBlank(message = "El NSS es obligatorio")
	@Size(min = 11, max = 11, message = "El NSS debe tener 11 caracteres")
	private String nss;

	@Size(max = 100)
	private String horarioLaboral;

	// ==========================================
	// CONTACTO DE EMERGENCIA
	// ==========================================
	@NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
	@Size(max = 150)
	private String contactoEmergenciaNombre;

	@NotBlank(message = "El teléfono del contacto de emergencia es obligatorio")
	@Size(max = 15)
	private String contactoEmergenciaTelefono;

	@NotBlank(message = "El parentesco del contacto de emergencia es obligatorio")
	@Size(max = 50)
	private String contactoEmergenciaParentesco;

	// ==========================================
	// OBSERVACIONES
	// ==========================================
	@Size(max = 500)
	private String observaciones;

	// ==========================================
	// ESTATUS Y AUDITORÍA
	// ==========================================
	private EstatusProfesor estatus;

	private Boolean activo;

	private Long usuarioId;

	// ==========================================
	// GETTERS Y SETTERS
	// ==========================================
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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
}