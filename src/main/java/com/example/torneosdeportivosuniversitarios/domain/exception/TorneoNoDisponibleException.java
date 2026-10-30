package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Regla 3: el torneo no esta EN_CURSO / disponible para la operacion (HTTP 409). */
public class TorneoNoDisponibleException extends ReglaNegocioException {

    public TorneoNoDisponibleException(String mensaje) {
        super("TORNEO_NO_DISPONIBLE", mensaje);
    }
}
