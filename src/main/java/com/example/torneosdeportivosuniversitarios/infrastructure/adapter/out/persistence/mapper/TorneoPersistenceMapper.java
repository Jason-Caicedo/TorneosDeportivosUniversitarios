package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.mapper;

import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoTorneo;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity.TorneoEntity;

/** Mapper Dominio <-> Entidad JPA para Torneo. */
public final class TorneoPersistenceMapper {

    private TorneoPersistenceMapper() {
    }

    public static Torneo toDomain(TorneoEntity entity) {
        if (entity == null) return null;
        return Torneo.builder()
                .idTorneo(entity.getIdTorneo())
                .nombre(entity.getNombre())
                .disciplina(entity.getDisciplina())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .estado(entity.getEstado() == null ? null : EstadoTorneo.valueOf(entity.getEstado()))
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static TorneoEntity toEntity(Torneo torneo) {
        if (torneo == null) return null;
        TorneoEntity entity = new TorneoEntity();
        entity.setIdTorneo(torneo.getIdTorneo());
        entity.setNombre(torneo.getNombre());
        entity.setDisciplina(torneo.getDisciplina());
        entity.setFechaInicio(torneo.getFechaInicio());
        entity.setFechaFin(torneo.getFechaFin());
        entity.setEstado(torneo.getEstado() == null ? null : torneo.getEstado().name());
        entity.setFechaCreacion(torneo.getFechaCreacion());
        return entity;
    }
}
