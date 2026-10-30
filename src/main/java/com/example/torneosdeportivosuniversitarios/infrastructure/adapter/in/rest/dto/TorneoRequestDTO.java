package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Datos para crear o actualizar un torneo")
public record TorneoRequestDTO(
        @Schema(example = "Copa Interfacultades 2026")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Schema(example = "Fútbol",
                description = "Valores permitidos: Fútbol, Fútbol sala, Voleibol, Baloncesto, Tenis de mesa, Ajedrez")
        @NotBlank(message = "La disciplina es obligatoria")
        String disciplina,

        @Schema(example = "2026-11-10", type = "string", format = "date")
        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @Schema(example = "2026-12-05", type = "string", format = "date")
        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin
) {
}
