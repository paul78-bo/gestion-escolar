package com.gestionescolar.api.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}