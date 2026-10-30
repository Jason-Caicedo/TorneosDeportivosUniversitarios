package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta del login con el token JWT")
public record JwtResponseDTO(
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...") String token,
        @Schema(example = "Bearer") String tipo,
        @Schema(example = "86400000") long expiraEnMs,
        UsuarioResponseDTO usuario
) {
}
