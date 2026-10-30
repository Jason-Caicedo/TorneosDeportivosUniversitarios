package com.example.torneosdeportivosuniversitarios.domain.exception;

/** El usuario existe pero su estado es BLOQUEADO (HTTP 403). */
public class UsuarioBloqueadoException extends DomainException {

    public UsuarioBloqueadoException(String email) {
        super("USUARIO_BLOQUEADO", "El usuario " + email + " esta BLOQUEADO");
    }
}
