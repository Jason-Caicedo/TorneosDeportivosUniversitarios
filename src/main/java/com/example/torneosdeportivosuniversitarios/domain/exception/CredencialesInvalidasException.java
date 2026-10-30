package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Email o password incorrectos en el login (HTTP 401). */
public class CredencialesInvalidasException extends DomainException {

    public CredencialesInvalidasException() {
        super("CREDENCIALES_INVALIDAS", "Email o password incorrectos");
    }
}
