package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.LoginRequestDTO;
import com.gestionescolar.api.dto.LoginResponseDTO;
import com.gestionescolar.api.dto.RegisterRequestDTO;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.enums.Rol;
import com.gestionescolar.api.repository.AlumnoRepository;
import com.gestionescolar.api.repository.ProfesorRepository;
import com.gestionescolar.api.repository.UsuarioRepository;
import com.gestionescolar.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AlumnoRepository alumnoRepository;
    private final ProfesorRepository profesorRepository;

    // 1. ACTUALIZAMOS EL CONSTRUCTOR
    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       AlumnoRepository alumnoRepository,
                       ProfesorRepository profesorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.alumnoRepository = alumnoRepository;
        this.profesorRepository = profesorRepository;
    }

    // ==========================================
    // REGISTRAR USUARIO
    // ==========================================
    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El username ya está en uso: " + dto.getUsername());
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está en uso: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRol().name(),
                jwtService.getJwtExpiration(),
                null
        );
    }

    // ==========================================
    // LOGIN
    // ==========================================
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);

        Long alumnoId = null;
        Long profesorId = null;

        if (usuario.getRol() == Rol.ALUMNO) {
            alumnoId = alumnoRepository.findByUsuario_Id(usuario.getId())
                    .map(a -> a.getId())
                    .orElse(null);
        } else if (usuario.getRol() == Rol.PROFESOR) {
            profesorId = profesorRepository.findByUsuarioId(usuario.getId())
                    .map(p -> p.getId())
                    .orElse(null);
        }

        return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRol().name(),
                jwtService.getJwtExpiration(),
                alumnoId
        );
    }
}