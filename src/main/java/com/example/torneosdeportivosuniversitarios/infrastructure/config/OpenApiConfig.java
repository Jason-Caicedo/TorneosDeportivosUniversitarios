package com.example.torneosdeportivosuniversitarios.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Documentacion Swagger / OpenAPI: http://localhost:8080/swagger-ui.html
 * Boton "Authorize": pegar SOLO el token (sin la palabra Bearer).
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Torneos Deportivos Universitarios",
                version = "1.0.0",
                description = "Proyecto Integrador - Programacion V. Arquitectura hexagonal, seguridad JWT por roles, "
                        + "cache y manejo centralizado de excepciones.",
                contact = @Contact(name = "Equipo Torneos UNIAJC")
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token obtenido en POST /api/auth/login"
)
public class OpenApiConfig {
}
