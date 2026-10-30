package com.example.torneosdeportivosuniversitarios.domain.model.enums;

import com.example.torneosdeportivosuniversitarios.domain.exception.ReglaNegocioException;

import java.util.Arrays;

public enum EstadoTorneo {
    PLANIFICADO,
    EN_CURSO,
    FINALIZADO;

    public static EstadoTorneo desdeTexto(String valor) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(valor == null ? "" : valor.trim()))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException(
                        "Estado de torneo no valido: '" + valor + "'. Valores permitidos: "
                                + Arrays.toString(values())));
    }
}
