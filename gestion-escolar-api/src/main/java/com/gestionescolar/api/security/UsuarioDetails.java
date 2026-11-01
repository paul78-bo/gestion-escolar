package com.gestionescolar.api.security;

import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.enums.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UsuarioDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    // ==========================================
    // MÉTODOS CUSTOM (para @PreAuthorize)
    // ==========================================
    
    /** ID del Usuario (tabla usuarios) */
    public Long getId() {
        return usuario.getId();
    }

    /** ID del Alumno asociado (si el usuario es ALUMNO) */
    public Long getAlumnoId() {
        if (usuario.getAlumno() != null) {
            return usuario.getAlumno().getId();
        }
        return null;
    }

    /** ID del Profesor asociado (si el usuario es PROFESOR) */
    public Long getProfesorId() {
        if (usuario.getProfesor() != null) {
            return usuario.getProfesor().getId();
        }
        return null;
    }

    public Rol getRol() {
        return usuario.getRol();
    }

    public String getEmail() {
        return usuario.getEmail();
    }

    // ==========================================
    // MÉTODOS DE UserDetails
    // ==========================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
        );
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getActivo();
    }
}