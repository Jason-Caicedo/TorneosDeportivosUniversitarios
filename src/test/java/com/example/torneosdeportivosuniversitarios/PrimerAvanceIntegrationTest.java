package com.example.torneosdeportivosuniversitarios;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba los requisitos del primer avance: seguridad JWT por roles, reglas de negocio,
 * manejo de excepciones y Swagger. Usa H2 en memoria (src/test/resources/application.yaml).
 */
@SpringBootTest
@AutoConfigureMockMvc
class PrimerAvanceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private String login(String email, String password) throws Exception {
        String body = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + mapper.readTree(body).get("token").asText();
    }

    @Test
    void flujoCompletoPrimerAvance() throws Exception {
        // Sin token -> 401
        mvc.perform(get("/api/torneos")).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("NO_AUTENTICADO"));

        // Registro publico -> 201, sin password en la respuesta, rol ESTUDIANTE
        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"jperez\",\"email\":\"jperez@uniajc.edu.co\",\"password\":\"Clave123*\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rol").value("ROLE_ESTUDIANTE"))
                .andExpect(jsonPath("$.password").doesNotExist());

        // Registro duplicado -> 409
        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"otro\",\"email\":\"jperez@uniajc.edu.co\",\"password\":\"Clave123*\"}"))
                .andExpect(status().isConflict());

        // Validacion -> 400 con errores por campo
        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"email\":\"malo\",\"password\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.email").exists());

        // Login incorrecto -> 401
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"jperez@uniajc.edu.co\",\"password\":\"mala\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("CREDENCIALES_INVALIDAS"));

        String estudiante = login("jperez@uniajc.edu.co", "Clave123*");
        String admin = login("admin@uniajc.edu.co", "Admin123*");

        mvc.perform(get("/api/auth/me").header("Authorization", estudiante))
                .andExpect(status().isOk()).andExpect(jsonPath("$.username").value("jperez"));

        String torneo = "{\"nombre\":\"Copa 2026\",\"disciplina\":\"futbol\",\"fechaInicio\":\"2026-11-10\",\"fechaFin\":\"2026-12-05\"}";

        // Estudiante NO puede crear torneo -> 403
        mvc.perform(post("/api/torneos").header("Authorization", estudiante)
                        .contentType(MediaType.APPLICATION_JSON).content(torneo))
                .andExpect(status().isForbidden());

        // Admin crea -> 201, disciplina normalizada
        String creado = mvc.perform(post("/api/torneos").header("Authorization", admin)
                        .contentType(MediaType.APPLICATION_JSON).content(torneo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.disciplina").value("Fútbol"))
                .andExpect(jsonPath("$.estado").value("PLANIFICADO"))
                .andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(creado).get("idTorneo").asLong();

        // Disciplina invalida -> 400 ; fechas invalidas -> 422
        mvc.perform(post("/api/torneos").header("Authorization", admin).contentType(MediaType.APPLICATION_JSON)
                        .content(torneo.replace("futbol", "Quidditch")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("DISCIPLINA_NO_VALIDA"));
        mvc.perform(post("/api/torneos").header("Authorization", admin).contentType(MediaType.APPLICATION_JSON)
                        .content(torneo.replace("2026-12-05", "2026-11-01")))
                .andExpect(status().isUnprocessableEntity());

        // Estudiante puede consultar (cache) y filtrar
        mvc.perform(get("/api/torneos?disciplina=Fútbol&estado=PLANIFICADO").header("Authorization", estudiante))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/torneos/" + id).header("Authorization", estudiante)).andExpect(status().isOk());
        mvc.perform(get("/api/torneos/9999").header("Authorization", estudiante)).andExpect(status().isNotFound());

        // Cerrar antes de iniciar -> 409 ; iniciar -> EN_CURSO ; cerrar -> FINALIZADO
        mvc.perform(put("/api/torneos/" + id + "/cerrar").header("Authorization", admin))
                .andExpect(status().isConflict());
        mvc.perform(put("/api/torneos/" + id + "/iniciar").header("Authorization", admin))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("EN_CURSO"));
        // la cache se actualiza: el GET devuelve el nuevo estado
        mvc.perform(get("/api/torneos/" + id).header("Authorization", estudiante))
                .andExpect(jsonPath("$.estado").value("EN_CURSO"));
        mvc.perform(get("/api/torneos?estado=PLANIFICADO").header("Authorization", estudiante))
                .andExpect(jsonPath("$.length()").value(0));
        mvc.perform(put("/api/torneos/" + id + "/cerrar").header("Authorization", admin))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("FINALIZADO"));

        // Roles: solo ADMIN
        mvc.perform(get("/api/roles").header("Authorization", estudiante)).andExpect(status().isForbidden());
        mvc.perform(get("/api/roles").header("Authorization", admin))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3));
        JsonNode rol = mapper.readTree(mvc.perform(post("/api/roles").header("Authorization", admin)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"entrenador\",\"descripcion\":\"x\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.nombre").value("ROLE_ENTRENADOR"))
                .andReturn().getResponse().getContentAsString());
        mvc.perform(get("/api/roles").header("Authorization", admin)).andExpect(jsonPath("$.length()").value(4));
        mvc.perform(delete("/api/roles/" + rol.get("idRol").asLong()).header("Authorization", admin))
                .andExpect(status().isNoContent());

        // Swagger publico
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }
}
