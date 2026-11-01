package com.gestionescolar.api.exception;

public class AsignacionNoEncontradaException extends RuntimeException {
    public AsignacionNoEncontradaException(Long id) {
        super("Asignación no encontrada con ID: " + id);
    }
}