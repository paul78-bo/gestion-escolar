package com.gestionescolar.api.controller;

import com.gestionescolar.api.dto.ForgotPasswordRequest;
import com.gestionescolar.api.dto.LoginRequestDTO;
import com.gestionescolar.api.dto.LoginResponseDTO;
import com.gestionescolar.api.dto.PasswordResetResponse;
import com.gestionescolar.api.dto.RegisterRequestDTO;
import com.gestionescolar.api.dto.ResetPasswordRequest;
import com.gestionescolar.api.service.AuthService;
import com.gestionescolar.api.service.PasswordResetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "API para login y registro")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService,PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
    
    // 1. Endpoint para solicitar el correo
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.forgotPassword(request);
            return ResponseEntity.ok(new PasswordResetResponse("Se ha enviado un enlace de recuperación a tu correo", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new PasswordResetResponse(e.getMessage(), false));
        }
    }

    // 2. Endpoint para validar el shortId (cuando el usuario abre el enlace del correo)
    @GetMapping("/validate-shortId")
    public ResponseEntity<PasswordResetResponse> validateShortId(@RequestParam String shortId) {
        try {
            boolean isValid = passwordResetService.validateShortId(shortId);
            if (isValid) {
                return ResponseEntity.ok(new PasswordResetResponse("Token válido", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PasswordResetResponse("Token inválido o expirado", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PasswordResetResponse("Error validando token", false));
        }
    }

    // 3. Endpoint para guardar la nueva contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok(new PasswordResetResponse("Contraseña restablecida exitosamente", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PasswordResetResponse(e.getMessage(), false));
        }
    }
    
    
}