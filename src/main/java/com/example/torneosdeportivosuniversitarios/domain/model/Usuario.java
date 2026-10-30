package com.example.torneosdeportivosuniversitarios.domain.model;

import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Modelo de dominio puro: cuenta de acceso con UN rol asignado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {

    private Long idUsuario;
    private String username;
    private String email;
    private String password;      // hash BCrypt, nunca se expone en la API
    private EstadoUsuario estado;
    private Rol rol;
    private Long idJugador;       // cuenta vinculada opcionalmente a un jugador
    private LocalDateTime fechaCreacion;

    public boolean estaActivo() {
        return EstadoUsuario.ACTIVO.equals(estado);
    }
}
