package com.example.torneosdeportivosuniversitarios.domain.exception;

/** El recurso solicitado no existe (HTTP 404). */
public class RecursoNoEncontradoException extends DomainException {

    public RecursoNoEncontradoException(String recurso, Object id) {
        super("RECURSO_NO_ENCONTRADO", recurso + " no encontrado con identificador: " + id);
    }
}
