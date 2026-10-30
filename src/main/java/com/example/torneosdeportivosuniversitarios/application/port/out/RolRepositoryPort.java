package com.example.torneosdeportivosuniversitarios.application.port.out;

import com.example.torneosdeportivosuniversitarios.domain.model.Rol;

import java.util.List;
import java.util.Optional;

/** Puerto de salida: persistencia de roles. */
public interface RolRepositoryPort {

    List<Rol> findAll();

    Optional<Rol> findById(Long idRol);

    Optional<Rol> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    Rol save(Rol rol);

    void deleteById(Long idRol);
}
