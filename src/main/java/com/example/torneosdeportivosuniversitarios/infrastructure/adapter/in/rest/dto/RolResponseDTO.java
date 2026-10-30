package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Rol del sistema")
public record RolResponseDTO(
        @Schema(example = "1") Long idRol,
        @Schema(example = "ROLE_ADMIN") String nombre,
        @Schema(example = "Administrador del sistema") String descripcion
) {
}
