package com.example.torneosdeportivosuniversitarios.domain.model;

import com.example.torneosdeportivosuniversitarios.domain.exception.EstadoTorneoInvalidoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.ReglaNegocioException;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.Disciplina;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoTorneo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de dominio puro: Torneo deportivo. Contiene sus propias reglas de negocio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Torneo implements Serializable {

    private Long idTorneo;
    private String nombre;
    private String disciplina;     // validada contra la lista controlada en codigo (enum Disciplina)
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoTorneo estado;
    private LocalDateTime fechaCreacion;

    /**
     * Valida los datos del torneo y normaliza la disciplina (regla de negocio 7).
     */
    public void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaNegocioException("El nombre del torneo es obligatorio");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new ReglaNegocioException("Las fechas de inicio y fin son obligatorias");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new ReglaNegocioException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        // Lanza DisciplinaNoValidaException si no esta en la lista permitida
        this.disciplina = Disciplina.desdeTexto(disciplina).getNombre();
    }

    /** Un torneo nuevo siempre inicia en estado PLANIFICADO. */
    public void inicializarComoNuevo() {
        validar();
        this.idTorneo = null;
        this.estado = EstadoTorneo.PLANIFICADO;
        this.fechaCreacion = LocalDateTime.now();
    }

    /** Actualiza los datos editables. Un torneo FINALIZADO no se puede modificar. */
    public void actualizarDatos(Torneo cambios) {
        if (EstadoTorneo.FINALIZADO.equals(estado)) {
            throw new EstadoTorneoInvalidoException("No se puede modificar un torneo FINALIZADO");
        }
        this.nombre = cambios.getNombre();
        this.disciplina = cambios.getDisciplina();
        this.fechaInicio = cambios.getFechaInicio();
        this.fechaFin = cambios.getFechaFin();
        validar();
    }

    /** PLANIFICADO -> EN_CURSO */
    public void iniciar() {
        if (!EstadoTorneo.PLANIFICADO.equals(estado)) {
            throw new EstadoTorneoInvalidoException(
                    "Solo se puede iniciar un torneo PLANIFICADO. Estado actual: " + estado);
        }
        this.estado = EstadoTorneo.EN_CURSO;
    }

    /** EN_CURSO -> FINALIZADO */
    public void cerrar() {
        if (!EstadoTorneo.EN_CURSO.equals(estado)) {
            throw new EstadoTorneoInvalidoException(
                    "Solo se puede cerrar un torneo EN_CURSO. Estado actual: " + estado);
        }
        this.estado = EstadoTorneo.FINALIZADO;
    }

    /** Usado por la regla 3: solo se registran resultados si el torneo esta EN_CURSO. */
    public boolean estaEnCurso() {
        return EstadoTorneo.EN_CURSO.equals(estado);
    }
}
