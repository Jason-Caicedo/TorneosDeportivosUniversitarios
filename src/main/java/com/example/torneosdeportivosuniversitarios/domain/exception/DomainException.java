package com.example.torneosdeportivosuniversitarios.domain.exception;

/**
 * Excepcion base de negocio. El dominio no conoce HTTP: el GlobalExceptionHandler
 * (adaptador de entrada) traduce cada tipo a un codigo de estado.
 */
public abstract class DomainException extends RuntimeException {

    private final String codigo;

    protected DomainException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
