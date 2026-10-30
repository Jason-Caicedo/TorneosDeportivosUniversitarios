package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar un usuario (se le asigna ROLE_ESTUDIANTE)")
public record UsuarioRequestDTO(
        @Schema(example = "jperez")
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
        String username,

        @Schema(example = "jperez@uniajc.edu.co")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        @Size(max = 150)
        String email,

        @Schema(example = "Clave123*")
        @NotBlank(message = "El password es obligatorio")
        @Size(min = 6, max = 100, message = "El password debe tener minimo 6 caracteres")
        String password
) {
}
