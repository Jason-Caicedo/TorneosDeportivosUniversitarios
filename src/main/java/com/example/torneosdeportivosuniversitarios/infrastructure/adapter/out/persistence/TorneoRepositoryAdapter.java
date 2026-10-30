package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence;

import com.example.torneosdeportivosuniversitarios.application.port.out.TorneoRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.model.Torneo;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoTorneo;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity.TorneoEntity;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.jpa.TorneoJpaRepository;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.mapper.TorneoPersistenceMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adaptador de salida: implementa el puerto TorneoRepositoryPort usando JPA. */
@Component
public class TorneoRepositoryAdapter implements TorneoRepositoryPort {

    private static final Sort ORDEN = Sort.by("fechaInicio").descending().and(Sort.by("idTorneo"));

    private final TorneoJpaRepository torneoJpaRepository;

    public TorneoRepositoryAdapter(TorneoJpaRepository torneoJpaRepository) {
        this.torneoJpaRepository = torneoJpaRepository;
    }

    @Override
    public Torneo save(Torneo torneo) {
        TorneoEntity guardado = torneoJpaRepository.save(TorneoPersistenceMapper.toEntity(torneo));
        return TorneoPersistenceMapper.toDomain(guardado);
    }

    @Override
    public Optional<Torneo> findById(Long idTorneo) {
        return torneoJpaRepository.findById(idTorneo).map(TorneoPersistenceMapper::toDomain);
    }

    @Override
    public List<Torneo> findAll(String disciplina, EstadoTorneo estado) {
        List<TorneoEntity> resultado;
        if (disciplina != null && estado != null) {
            resultado = torneoJpaRepository.findByDisciplinaAndEstado(disciplina, estado.name(), ORDEN);
        } else if (disciplina != null) {
            resultado = torneoJpaRepository.findByDisciplina(disciplina, ORDEN);
        } else if (estado != null) {
            resultado = torneoJpaRepository.findByEstado(estado.name(), ORDEN);
        } else {
            resultado = torneoJpaRepository.findAll(ORDEN);
        }
        return resultado.stream().map(TorneoPersistenceMapper::toDomain).toList();
    }
}
