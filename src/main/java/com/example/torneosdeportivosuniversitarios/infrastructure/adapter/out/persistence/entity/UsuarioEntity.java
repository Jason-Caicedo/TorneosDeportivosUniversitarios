package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rol;

    /** FK a jugador (nullable). Se mapea como columna simple hasta implementar Jugador. */
    @Column(name = "id_jugador")
    private Long idJugador;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = "ACTIVO";
    }
}
