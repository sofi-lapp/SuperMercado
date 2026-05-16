package com.uade.supermercado.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(String.format("%s con id=%d no encontrado", recurso, id));
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
