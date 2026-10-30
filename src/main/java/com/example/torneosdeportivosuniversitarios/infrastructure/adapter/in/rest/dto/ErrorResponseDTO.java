package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Formato estandar de error de la API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        @Schema(example = "2026-10-30T10:15:30") LocalDateTime timestamp,
        @Schema(example = "409") int status,
        @Schema(example = "Conflict") String error,
        @Schema(example = "ESTADO_TORNEO_INVALIDO") String codigo,
        @Schema(example = "Solo se puede iniciar un torneo PLANIFICADO. Estado actual: FINALIZADO") String mensaje,
        @Schema(example = "/api/torneos/1/iniciar") String path,
        @Schema(description = "Errores de validacion por campo (solo en 400)") Map<String, String> errores
) {
    public static ErrorResponseDTO of(HttpStatus status, String codigo, String mensaje, String path) {
        return of(status, codigo, mensaje, path, null);
    }

    public static ErrorResponseDTO of(HttpStatus status, String codigo, String mensaje, String path,
                                      Map<String, String> errores) {
        return new ErrorResponseDTO(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                codigo, mensaje, path, errores);
    }
}
