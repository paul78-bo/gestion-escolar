package com.gestionescolar.api.exception;

public class MateriaNoEncontradaException extends RuntimeException {

    public MateriaNoEncontradaException(Long id) {
        super("Materia no encontrada con ID: " + id);
    }

    public MateriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}