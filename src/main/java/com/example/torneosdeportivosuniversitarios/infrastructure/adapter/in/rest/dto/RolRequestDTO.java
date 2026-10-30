package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos de un rol. Si no empieza por ROLE_ se agrega automaticamente")
public record RolRequestDTO(
        @Schema(example = "ROLE_ENTRENADOR")
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 30, message = "El nombre del rol no puede superar 30 caracteres")
        String nombre,

        @Schema(example = "Entrenador de equipo")
        @Size(max = 150)
        String descripcion
) {
}
