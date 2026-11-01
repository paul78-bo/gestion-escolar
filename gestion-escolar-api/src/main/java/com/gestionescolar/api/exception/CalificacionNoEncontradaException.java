package com.gestionescolar.api.exception;

public class CalificacionNoEncontradaException extends RuntimeException {
    public CalificacionNoEncontradaException(Long id) {
        super("Calificación no encontrada con ID: " + id);
    }
}