package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.mapper;

import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.TorneoRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.TorneoResponseDTO;

public final class TorneoRestMapper {

    private TorneoRestMapper() {
    }

    public static Torneo toDomain(TorneoRequestDTO dto) {
        return Torneo.builder()
                .nombre(dto.nombre())
                .disciplina(dto.disciplina())
                .fechaInicio(dto.fechaInicio())
                .fechaFin(dto.fechaFin())
                .build();
    }

    public static TorneoResponseDTO toResponse(Torneo t) {
        return new TorneoResponseDTO(
                t.getIdTorneo(),
                t.getNombre(),
                t.getDisciplina(),
                t.getFechaInicio(),
                t.getFechaFin(),
                t.getEstado() == null ? null : t.getEstado().name(),
                t.getFechaCreacion());
    }
}
