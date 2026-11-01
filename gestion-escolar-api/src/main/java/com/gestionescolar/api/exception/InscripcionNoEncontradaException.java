package com.gestionescolar.api.exception;

public class InscripcionNoEncontradaException extends RuntimeException {

    public InscripcionNoEncontradaException(Long id) {
        super("Inscripción no encontrada con ID: " + id);
    }

    public InscripcionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}