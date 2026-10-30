package com.example.torneosdeportivosuniversitarios.infrastructure.security;

import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Respuestas JSON para errores de seguridad que ocurren en el filtro (antes del controlador):
 *  401 -> no hay token o es invalido/expirado
 *  403 -> el usuario no tiene el rol requerido
 */
@Component
public class RestSecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestSecurityErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        escribir(response, request, HttpStatus.UNAUTHORIZED, "NO_AUTENTICADO",
                "Debe enviar un token JWT valido en el header Authorization: Bearer <token>");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        escribir(response, request, HttpStatus.FORBIDDEN, "ACCESO_DENEGADO",
                "No tiene permisos para realizar esta operacion");
    }

    private void escribir(HttpServletResponse response, HttpServletRequest request,
                          HttpStatus status, String codigo, String mensaje) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(),
                ErrorResponseDTO.of(status, codigo, mensaje, request.getRequestURI()));
    }
}
