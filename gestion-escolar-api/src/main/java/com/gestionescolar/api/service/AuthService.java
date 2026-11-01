package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.LoginRequestDTO;
import com.gestionescolar.api.dto.LoginResponseDTO;
import com.gestionescolar.api.dto.RegisterRequestDTO;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.enums.Rol;
import com.gestionescolar.api.repository.AlumnoRepository; // 👈 NUEVO
import com.gestionescolar.api.repository.ProfesorRepository; // 👈 NUEVO
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
    private final AlumnoRepository alumnoRepository;     // 👈 NUEVO
    private final ProfesorRepository profesorRepository; // 👈 NUEVO

    // 1. ACTUALIZAMOS EL CONSTRUCTOR
    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       AlumnoRepository alumnoRepository,     // 👈 NUEVO
                       ProfesorRepository profesorRepository) { // 👈 NUEVO
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.alumnoRepository = alumnoRepository;     // 👈 NUEVO
        this.profesorRepository = profesorRepository; // 👈 NUEVO
    }

    // ==========================================
    // REGISTRAR USUARIO
    // ==========================================
    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO dto) {
        // Validar duplicados
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El username ya está en uso: " + dto.getUsername());
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está en uso: " + dto.getEmail());
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        // Generar token
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);

        // 2. ACTUALIZAMOS LA RESPUESTA (Pasamos null porque es un registro nuevo)
        return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRol().name(),
                jwtService.getJwtExpiration(),
                null // alumnoId (es null porque apenas se está registrando)
        );
    }

    // ==========================================
    // LOGIN
    // ==========================================
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        // Cargar usuario
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);

        // 3. LÓGICA PARA BUSCAR EL ID DEL ALUMNO O PROFESOR 👈
        Long alumnoId = null;
        Long profesorId = null; // (Lo dejamos listo para el futuro)

        if (usuario.getRol() == Rol.ALUMNO) {
            alumnoId = alumnoRepository.findByUsuario_Id(usuario.getId())
                    .map(a -> a.getId())
                    .orElse(null);
        } else if (usuario.getRol() == Rol.PROFESOR) {
            profesorId = profesorRepository.findByUsuarioId(usuario.getId())
                    .map(p -> p.getId())
                    .orElse(null);
        }

        // 4. RETORNAMOS LA RESPUESTA CON EL ALUMNO ID 👈
        return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRol().name(),
                jwtService.getJwtExpiration(),
                alumnoId // Aquí va el ID que encontramos (o null si es admin)
        );
    }
}