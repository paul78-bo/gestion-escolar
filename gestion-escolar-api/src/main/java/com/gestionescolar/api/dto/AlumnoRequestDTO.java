package com.gestionescolar.api.dto;

import java.time.LocalDate;

import com.gestionescolar.api.enums.EstatusAlumno;
import com.gestionescolar.api.enums.Genero;

import jakarta.validation.constraints.*;

public class AlumnoRequestDTO {

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
	@Size(min = 18, max = 18)
	private String curp;

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email debe ser válido")
	@Size(max = 150)
	private String email;

	@NotBlank(message = "El teléfono es obligatorio")
	@Size(max = 15)
	private String telefono;

	@NotNull(message = "El género es obligatorio")
	private Genero genero;

	@NotNull(message = "La fecha de nacimiento es obligatoria")
	@Past(message = "La fecha de nacimiento debe ser en el pasado")
	private LocalDate fechaNacimiento;

	@NotBlank(message = "El estado civil es obligatorio")
	@Size(max = 20)
	private String estadoCivil;

	@NotBlank(message = "La nacionalidad es obligatoria")
	@Size(max = 50)
	private String nacionalidad;

	@Size(max = 500)
	private String observaciones;

	@NotBlank(message = "El país de nacimiento es obligatorio")
	@Size(max = 50)
	private String paisNacimiento;

	@NotBlank(message = "La entidad de nacimiento es obligatoria")
	@Size(max = 100)
	private String entidadNacimiento;

	@NotBlank(message = "El municipio de nacimiento es obligatorio")
	@Size(max = 100)
	private String municipioNacimiento;

	@Size(max = 100)
	private String localidadNacimiento;

	@NotBlank(message = "La calle es obligatoria")
	@Size(max = 100)
	private String calle;

	@NotBlank(message = "El número exterior es obligatorio")
	@Size(max = 10)
	private String numeroExterior;

	@Size(max = 10)
	private String numeroInterior;

	@NotBlank(message = "La colonia es obligatoria")
	@Size(max = 100)
	private String colonia;

	@NotBlank(message = "El código postal es obligatorio")
	@Size(max = 10)
	private String codigoPostal;

	@NotBlank(message = "La localidad es obligatoria")
	@Size(max = 100)
	private String localidad;

	@NotBlank(message = "El municipio es obligatorio")
	@Size(max = 100)
	private String municipio;

	@NotBlank(message = "La entidad federativa es obligatoria")
	@Size(max = 100)
	private String entidadFederativa;

	@NotBlank(message = "La matrícula es obligatoria")
	@Size(max = 20)
	private String matricula;

	private EstatusAlumno estatus;

	private LocalDate fechaIngreso;

	@NotBlank(message = "El nombre del tutor es obligatorio")
	@Size(max = 150)
	private String tutorNombre;

	@NotBlank(message = "El teléfono del tutor es obligatorio")
	@Size(max = 15)
	private String tutorTelefono;

	@NotBlank(message = "El email del tutor es obligatorio")
	@Email(message = "El email del tutor debe ser válido")
	@Size(max = 150)
	private String tutorEmail;

	private Boolean activo;
	private Long usuarioId;

	// --- GETTERS Y SETTERS ---
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

	public String getMunicipioNacimiento() {
		return municipioNacimiento;
	}

	public void setMunicipioNacimiento(String municipioNacimiento) {
		this.municipioNacimiento = municipioNacimiento;
	}

	public String getLocalidadNacimiento() {
		return localidadNacimiento;
	}

	public void setLocalidadNacimiento(String localidadNacimiento) {
		this.localidadNacimiento = localidadNacimiento;
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

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEntidadFederativa() {
		return entidadFederativa;
	}

	public void setEntidadFederativa(String entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
}