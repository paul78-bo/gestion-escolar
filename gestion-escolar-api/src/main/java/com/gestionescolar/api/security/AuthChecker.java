// Archivo: com/gestionescolar/api/security/AuthChecker.java
package com.gestionescolar.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authChecker")
public class AuthChecker {
    public boolean esMismoAlumno(Authentication auth, Long alumnoId) {
        // 👇 Asegúrate de tener el auth != null
        if (auth != null && auth.getPrincipal() instanceof UsuarioDetails u) {
            return alumnoId != null && alumnoId.equals(u.getAlumnoId());
        }
        return false;
    }
}