package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/** Regla 8: el DTO de salida NO incluye el password. */
@Schema(description = "Usuario (sin password)")
public record UsuarioResponseDTO(
        @Schema(example = "1") Long idUsuario,
        @Schema(example = "jperez") String username,
        @Schema(example = "jperez@uniajc.edu.co") String email,
        @Schema(example = "ACTIVO") String estado,
        @Schema(example = "ROLE_ESTUDIANTE") String rol,
        Long idJugador,
        LocalDateTime fechaCreacion
) {
}
