package com.example.torneosdeportivosuniversitarios.application.port.out;

import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoTorneo;

import java.util.List;
import java.util.Optional;

/** Puerto de salida: persistencia de torneos. */
public interface TorneoRepositoryPort {

    Torneo save(Torneo torneo);

    Optional<Torneo> findById(Long idTorneo);

    /** Ambos filtros son opcionales (null = sin filtro). */
    List<Torneo> findAll(String disciplina, EstadoTorneo estado);
}
