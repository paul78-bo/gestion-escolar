package com.gestionescolar.api.exception;

public class AlumnoNoEncontradoException extends RuntimeException {

    public AlumnoNoEncontradoException(Long id) {
        super("Alumno no encontrado con ID: " + id);
    }

    public AlumnoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}