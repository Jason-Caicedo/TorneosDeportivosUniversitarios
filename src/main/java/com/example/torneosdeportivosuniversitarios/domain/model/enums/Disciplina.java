package com.example.torneosdeportivosuniversitarios.domain.model.enums;

import com.example.torneosdeportivosuniversitarios.domain.exception.DisciplinaNoValidaException;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

/**
 * Lista controlada de disciplinas (regla de negocio 7): se valida en codigo, NO en la BD.
 * En la base de datos se guarda el nombre legible (ej: "Fútbol").
 */
public enum Disciplina {
    FUTBOL("Fútbol"),
    FUTBOL_SALA("Fútbol sala"),
    VOLEIBOL("Voleibol"),
    BALONCESTO("Baloncesto"),
    TENIS_DE_MESA("Tenis de mesa"),
    AJEDREZ("Ajedrez");

    private final String nombre;

    Disciplina(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Acepta "Fútbol", "futbol", "FUTBOL", "Futbol Sala", "FUTBOL_SALA", etc.
     */
    public static Disciplina desdeTexto(String valor) {
        String buscado = normalizar(valor);
        return Arrays.stream(values())
                .filter(d -> d.name().equals(buscado) || normalizar(d.nombre).equals(buscado))
                .findFirst()
                .orElseThrow(() -> new DisciplinaNoValidaException(valor, nombresPermitidos()));
    }

    public static List<String> nombresPermitidos() {
        return Arrays.stream(values()).map(Disciplina::getNombre).toList();
    }

    private static String normalizar(String texto) {
        if (texto == null) return "";
        String sinTildes = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.toUpperCase().replaceAll("[\\s-]+", "_");
    }
}
