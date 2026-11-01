package com.gestionescolar.api.exception;

public class GrupoNoEncontradoException extends RuntimeException {

    public GrupoNoEncontradoException(Long id) {
        super("Grupo no encontrado con ID: " + id);
    }

    public GrupoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}