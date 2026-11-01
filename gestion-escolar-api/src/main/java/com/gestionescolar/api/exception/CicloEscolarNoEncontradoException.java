package com.gestionescolar.api.exception;

public class CicloEscolarNoEncontradoException extends RuntimeException {
    public CicloEscolarNoEncontradoException(Long id) {
        super("Ciclo escolar no encontrado con ID: " + id);
    }
}