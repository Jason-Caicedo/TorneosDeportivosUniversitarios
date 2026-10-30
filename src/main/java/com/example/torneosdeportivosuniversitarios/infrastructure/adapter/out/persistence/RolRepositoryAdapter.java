package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence;

import com.example.torneosdeportivosuniversitarios.application.port.out.RolRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.jpa.RolJpaRepository;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.mapper.RolPersistenceMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adaptador de salida: implementa el puerto RolRepositoryPort usando JPA. */
@Component
public class RolRepositoryAdapter implements RolRepositoryPort {

    private final RolJpaRepository rolJpaRepository;

    public RolRepositoryAdapter(RolJpaRepository rolJpaRepository) {
        this.rolJpaRepository = rolJpaRepository;
    }

    @Override
    public List<Rol> findAll() {
        return rolJpaRepository.findAll(Sort.by("idRol")).stream()
                .map(RolPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Rol> findById(Long idRol) {
        return rolJpaRepository.findById(idRol).map(RolPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Rol> findByNombre(String nombre) {
        return rolJpaRepository.findByNombre(nombre).map(RolPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return rolJpaRepository.existsByNombre(nombre);
    }

    @Override
    public Rol save(Rol rol) {
        return RolPersistenceMapper.toDomain(rolJpaRepository.save(RolPersistenceMapper.toEntity(rol)));
    }

    @Override
    public void deleteById(Long idRol) {
        rolJpaRepository.deleteById(idRol);
    }
}
