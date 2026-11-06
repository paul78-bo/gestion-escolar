package com.gestionescolar.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 👇 1. NUEVA VALIDACIÓN: Ignorar el filtro para rutas públicas de recuperación
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/") || path.startsWith("/reset-password") || path.startsWith("/api/auth/validate-shortId")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 3. Si no hay header o no empieza con "Bearer ", continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Extraer el token
        jwt = authHeader.substring(7);

        try {
            // 5. Extraer el username
            username = jwtService.extractUsername(jwt);

            // 6. Si hay username y no hay autenticación en el contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 7. Cargar el usuario
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 8. Validar el token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    
                    // 9. Crear la autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 10. Establecer la autenticación en el contexto
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token inválido o expirado
            logger.error("Error validando token JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}