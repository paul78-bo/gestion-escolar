package com.gestionescolar.api.exception;

public class ProfesorNoEncontradoException extends RuntimeException {

    public ProfesorNoEncontradoException(Long id) {
        super("Profesor no encontrado con ID: " + id);
    }

    public ProfesorNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}