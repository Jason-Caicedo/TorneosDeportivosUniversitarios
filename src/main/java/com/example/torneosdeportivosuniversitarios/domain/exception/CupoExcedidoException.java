package com.example.torneosdeportivosuniversitarios.domain.exception;

/** Regla 2: el equipo no puede exceder su cupo_maximo_jugadores (HTTP 409). */
public class CupoExcedidoException extends ReglaNegocioException {

    public CupoExcedidoException(String nombreEquipo, int cupoMaximo) {
        super("CUPO_EXCEDIDO",
                "El equipo " + nombreEquipo + " ya alcanzo su cupo maximo de " + cupoMaximo + " jugadores");
    }
}
