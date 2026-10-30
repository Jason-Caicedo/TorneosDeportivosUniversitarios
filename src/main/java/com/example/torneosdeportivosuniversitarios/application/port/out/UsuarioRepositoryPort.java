package com.example.torneosdeportivosuniversitarios.application.port.out;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;

import java.util.Optional;

/** Puerto de salida: persistencia de usuarios (lo implementa el adaptador JPA). */
public interface UsuarioRepositoryPort {

    Usuario save(Usuario usuario);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByRol(Long idRol);
}
