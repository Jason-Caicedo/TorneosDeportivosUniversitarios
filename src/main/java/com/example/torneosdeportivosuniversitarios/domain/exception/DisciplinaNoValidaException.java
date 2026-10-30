package com.example.torneosdeportivosuniversitarios.domain.exception;

import java.util.List;

/** Regla 7: la disciplina debe estar en la lista controlada en codigo (HTTP 400). */
public class DisciplinaNoValidaException extends ReglaNegocioException {

    public DisciplinaNoValidaException(String valor, List<String> permitidas) {
        super("DISCIPLINA_NO_VALIDA",
                "La disciplina '" + valor + "' no es valida. Disciplinas permitidas: " + permitidas);
    }
}
