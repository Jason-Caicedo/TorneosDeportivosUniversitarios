package com.example.torneosdeportivosuniversitarios.application.port.in;

import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;

public interface ActualizarTorneoUseCase {

    Torneo actualizar(Long idTorneo, Torneo cambios);

    /** PLANIFICADO -> EN_CURSO */
    Torneo iniciar(Long idTorneo);

    /** EN_CURSO -> FINALIZADO */
    Torneo cerrar(Long idTorneo);
}
