package com.example.torneosdeportivosuniversitarios.application.services;

import com.example.torneosdeportivosuniversitarios.application.port.in.ActualizarTorneoUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.in.ConsultarTorneoUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.in.CrearTorneoUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.out.TorneoRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoNoEncontradoException;
import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.Disciplina;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoTorneo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Casos de uso de Torneo con cache:
 *  - "torneo"   : torneo por id
 *  - "torneos"  : listados (la llave combina los filtros disciplina + estado)
 * Toda escritura actualiza el torneo en cache y limpia los listados.
 */
@Service
public class TorneoService implements CrearTorneoUseCase, ConsultarTorneoUseCase, ActualizarTorneoUseCase {

    private final TorneoRepositoryPort torneoRepositoryPort;

    public TorneoService(TorneoRepositoryPort torneoRepositoryPort) {
        this.torneoRepositoryPort = torneoRepositoryPort;
    }

    @Override
    @Transactional
    @CacheEvict(value = "torneos", allEntries = true)
    public Torneo crear(Torneo torneo) {
        torneo.inicializarComoNuevo();   // valida reglas + estado PLANIFICADO
        return torneoRepositoryPort.save(torneo);
    }

    @Override
    @Cacheable(value = "torneos", key = "(#disciplina ?: 'TODAS') + '-' + (#estado ?: 'TODOS')")
    public List<Torneo> listar(String disciplina, String estado) {
        String disciplinaFiltro = esVacio(disciplina) ? null : Disciplina.desdeTexto(disciplina).getNombre();
        EstadoTorneo estadoFiltro = esVacio(estado) ? null : EstadoTorneo.desdeTexto(estado);
        return torneoRepositoryPort.findAll(disciplinaFiltro, estadoFiltro);
    }

    @Override
    @Cacheable(value = "torneo", key = "#idTorneo")
    public Torneo obtener(Long idTorneo) {
        return buscar(idTorneo);
    }

    @Override
    @Transactional
    @Caching(put = @CachePut(value = "torneo", key = "#idTorneo"),
             evict = @CacheEvict(value = "torneos", allEntries = true))
    public Torneo actualizar(Long idTorneo, Torneo cambios) {
        Torneo torneo = buscar(idTorneo);
        torneo.actualizarDatos(cambios);
        return torneoRepositoryPort.save(torneo);
    }

    @Override
    @Transactional
    @Caching(put = @CachePut(value = "torneo", key = "#idTorneo"),
             evict = @CacheEvict(value = "torneos", allEntries = true))
    public Torneo iniciar(Long idTorneo) {
        Torneo torneo = buscar(idTorneo);
        torneo.iniciar();
        return torneoRepositoryPort.save(torneo);
    }

    @Override
    @Transactional
    @Caching(put = @CachePut(value = "torneo", key = "#idTorneo"),
             evict = @CacheEvict(value = "torneos", allEntries = true))
    public Torneo cerrar(Long idTorneo) {
        Torneo torneo = buscar(idTorneo);
        torneo.cerrar();
        return torneoRepositoryPort.save(torneo);
    }

    private Torneo buscar(Long idTorneo) {
        return torneoRepositoryPort.findById(idTorneo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Torneo", idTorneo));
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
