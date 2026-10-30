package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Transicion de estado no permitida en un torneo (HTTP 409). */
public class EstadoTorneoInvalidoException extends ReglaNegocioException {

    public EstadoTorneoInvalidoException(String mensaje) {
        super("ESTADO_TORNEO_INVALIDO", mensaje);
    }
}
