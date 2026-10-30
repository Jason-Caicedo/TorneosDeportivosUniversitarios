package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Regla 1: un jugador no puede inscribirse dos veces en el mismo torneo (HTTP 409). */
public class InscripcionDuplicadaException extends ReglaNegocioException {

    public InscripcionDuplicadaException(Long idJugador, Long idTorneo) {
        super("INSCRIPCION_DUPLICADA",
                "El jugador " + idJugador + " ya esta inscrito en el torneo " + idTorneo);
    }
}
