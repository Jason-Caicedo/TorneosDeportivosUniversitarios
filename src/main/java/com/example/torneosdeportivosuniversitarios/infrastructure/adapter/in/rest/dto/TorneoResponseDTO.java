package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Torneo")
public record TorneoResponseDTO(
        @Schema(example = "1") Long idTorneo,
        @Schema(example = "Copa Interfacultades 2026") String nombre,
        @Schema(example = "Fútbol") String disciplina,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        @Schema(example = "PLANIFICADO") String estado,
        LocalDateTime fechaCreacion
) {
}
