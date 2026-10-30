package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Ya existe un registro con un valor que debe ser unico (HTTP 409). */
public class RecursoDuplicadoException extends DomainException {

    public RecursoDuplicadoException(String mensaje) {
        super("RECURSO_DUPLICADO", mensaje);
    }
}
