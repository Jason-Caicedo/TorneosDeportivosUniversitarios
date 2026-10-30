package com.example.torneosdeportivosuniversitarios.application.port.in;

import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;

import java.util.List;

public interface ConsultarTorneoUseCase {

    /** disciplina y estado son filtros opcionales (pueden ser null). */
    List<Torneo> listar(String disciplina, String estado);

    Torneo obtener(Long idTorneo);
}
