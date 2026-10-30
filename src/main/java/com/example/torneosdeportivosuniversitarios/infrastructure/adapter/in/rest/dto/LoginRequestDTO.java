package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales de acceso")
public record LoginRequestDTO(
        @Schema(example = "admin@uniajc.edu.co")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,

        @Schema(example = "Admin123*")
        @NotBlank(message = "El password es obligatorio")
        String password
) {
}
