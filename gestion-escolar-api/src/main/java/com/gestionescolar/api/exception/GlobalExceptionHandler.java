package com.gestionescolar.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setError("Forbidden");
        error.setMessage("Access Denied: No tienes permisos para acceder a este recurso");
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Validation Error");
        error.setMessage("Error de validación en los campos");
        error.setPath(request.getRequestURI());
        error.setValidationErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ==========================================
    // NOT FOUND (404)
    // ==========================================

    @ExceptionHandler(AlumnoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleAlumnoNoEncontrado(AlumnoNoEncontradoException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    @ExceptionHandler(ProfesorNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProfesorNoEncontrado(ProfesorNoEncontradoException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    @ExceptionHandler(MateriaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleMateriaNoEncontrada(MateriaNoEncontradaException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    @ExceptionHandler(GrupoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleGrupoNoEncontrado(GrupoNoEncontradoException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    @ExceptionHandler(InscripcionNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleInscripcionNoEncontrada(InscripcionNoEncontradaException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    // ✅ NUEVOS
    @ExceptionHandler(CicloEscolarNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleCicloNoEncontrado(CicloEscolarNoEncontradoException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    @ExceptionHandler(AsignacionNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleAsignacionNoEncontrada(AsignacionNoEncontradaException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    @ExceptionHandler(CalificacionNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCalificacionNoEncontrada(CalificacionNoEncontradaException ex, HttpServletRequest request) {
        return notFound(ex, request);
    }

    // ==========================================
    // BAD REQUEST (400)
    // ==========================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Bad Request");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnsupported(UnsupportedOperationException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.GONE.value());
        error.setError("Gone");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.GONE).body(error);
    }

    // ==========================================
    // INTERNAL SERVER ERROR (500)
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError("Internal Server Error");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ==========================================
    // HELPER
    // ==========================================

    private ResponseEntity<ErrorResponse> notFound(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Not Found");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}