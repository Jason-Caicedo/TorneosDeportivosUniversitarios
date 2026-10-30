package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Violacion generica de una regla de negocio (HTTP 422). */
public class ReglaNegocioException extends DomainException {

    public ReglaNegocioException(String mensaje) {
        super("REGLA_NEGOCIO", mensaje);
    }

    protected ReglaNegocioException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
