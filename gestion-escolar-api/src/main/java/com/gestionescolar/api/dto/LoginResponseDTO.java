package com.gestionescolar.api.dto;

public class LoginResponseDTO {

	private String token;
	private String tipo = "Bearer";
	private String username;
	private String rol;
	private Long expiresIn;

	private Long alumnoId; // 👈 NUEVO CAMPO: Para saber el ID del alumno logueado

	public LoginResponseDTO() {
	}

	public LoginResponseDTO(String token, String username, String rol, Long expiresIn, Long alumnoId) {
		this.token = token;
		this.username = username;
		this.rol = rol;
		this.expiresIn = expiresIn;
		this.alumnoId=alumnoId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}
	
	
}