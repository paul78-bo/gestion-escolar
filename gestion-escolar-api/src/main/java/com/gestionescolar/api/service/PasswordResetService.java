package com.gestionescolar.api.service;

import com.gestionescolar.api.dto.ForgotPasswordRequest;
import com.gestionescolar.api.dto.ResetPasswordRequest;
import com.gestionescolar.api.exception.PasswordValidationException;
import com.gestionescolar.api.exception.TokenExpiradoException;
import com.gestionescolar.api.exception.TokenInvalidoException;
import com.gestionescolar.api.exception.UsuarioNoEncontradoException;
import com.gestionescolar.api.entity.PasswordResetToken;
import com.gestionescolar.api.entity.Usuario;
import com.gestionescolar.api.repository.PasswordResetTokenRepository;
import com.gestionescolar.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class PasswordResetService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${app.reset-token-expiry-hours:24}")
    private int tokenExpiryHours;
    
    @Value("${app.frontend-url:http://localhost:8084}")
    private String frontendUrl;

    public void validatePassword(String password) {
        if (password == null) {
            throw new PasswordValidationException("La contraseña no puede ser nula");
        }
        if (password.length() < 8) {
            throw new PasswordValidationException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new PasswordValidationException("Debe contener al menos una letra mayúscula (A-Z)");
        }
        if (!password.matches(".*\\d.*")) {
            throw new PasswordValidationException("Debe contener al menos un número (0-9)");
        }
        
        String simbolosPermitidos = "!@#$%^&*()_+-=[]{};':\"|,.<>/?";
        long countSimbolos = password.chars()
            .filter(ch -> simbolosPermitidos.indexOf(ch) >= 0)
            .count();
        
        if (countSimbolos < 2) {
            throw new PasswordValidationException(
                String.format("Debe contener al menos 2 símbolos especiales. Permitidos: %s", simbolosPermitidos)
            );
        }
    }
    
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con email: " + email));
        
        // Borrar tokens anteriores para no acumular basura
        tokenRepository.deleteByUsuario(usuario);
        
        PasswordResetToken resetToken = new PasswordResetToken(usuario, LocalDateTime.now().plusHours(tokenExpiryHours));
        tokenRepository.save(resetToken);
        
        emailService.sendPasswordResetEmail(usuario.getEmail(), resetToken.getShortId());
    }
    
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String shortId = request.getShortId();
        String newPassword = request.getNewPassword();
        
        validatePassword(newPassword);
        
        PasswordResetToken resetToken = tokenRepository.findByShortId(shortId)
                .orElseThrow(() -> new TokenInvalidoException("Token inválido"));
        
        if (resetToken.isUsed()) {
            throw new TokenInvalidoException("Token ya ha sido utilizado");
        }
        
        if (resetToken.isExpired()) {
            throw new TokenExpiradoException("Token ha expirado");
        }
        
        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        
        tokenRepository.deleteByUsuario(usuario);
    }
    
    public boolean validateShortId(String shortId) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByShortId(shortId).orElse(null);
            
            if (resetToken == null || resetToken.isUsed()) return false;
            
            if (resetToken.isExpired()) {
                tokenRepository.delete(resetToken);
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}