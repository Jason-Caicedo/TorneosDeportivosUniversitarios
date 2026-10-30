package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "torneo")
public class TorneoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_torneo")
    private Long idTorneo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "disciplina", nullable = false, length = 50)
    private String disciplina;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = "PLANIFICADO";
    }
}
