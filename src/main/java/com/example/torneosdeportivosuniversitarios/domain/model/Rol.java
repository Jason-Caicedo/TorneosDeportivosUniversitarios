package com.example.torneosdeportivosuniversitarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Modelo de dominio puro: Rol del sistema (ROLE_ADMIN, ROLE_ARBITRO, ROLE_ESTUDIANTE).
 * No depende de Spring ni de JPA. Implementa Serializable para poder guardarse en cache (Redis).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol implements Serializable {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String ARBITRO = "ROLE_ARBITRO";
    public static final String ESTUDIANTE = "ROLE_ESTUDIANTE";

    private Long idRol;
    private String nombre;
    private String descripcion;
}
