package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.jpa;

import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity.TorneoEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TorneoJpaRepository extends JpaRepository<TorneoEntity, Long> {

    List<TorneoEntity> findByDisciplina(String disciplina, Sort sort);

    List<TorneoEntity> findByEstado(String estado, Sort sort);

    List<TorneoEntity> findByDisciplinaAndEstado(String disciplina, String estado, Sort sort);
}
