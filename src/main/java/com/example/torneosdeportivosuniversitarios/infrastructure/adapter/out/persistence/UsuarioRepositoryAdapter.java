package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence;

import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoNoEncontradoException;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.jpa.RolJpaRepository;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.jpa.UsuarioJpaRepository;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/** Adaptador de salida: implementa el puerto UsuarioRepositoryPort usando JPA. */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final RolJpaRepository rolJpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository, RolJpaRepository rolJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.rolJpaRepository = rolJpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioPersistenceMapper.toEntity(usuario);
        Long idRol = usuario.getRol().getIdRol();
        entity.setRol(rolJpaRepository.findById(idRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", idRol)));
        return UsuarioPersistenceMapper.toDomain(usuarioJpaRepository.save(entity));
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioJpaRepository.findByEmailIgnoreCase(email).map(UsuarioPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioJpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioJpaRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public boolean existsByRol(Long idRol) {
        return usuarioJpaRepository.existsByRol_IdRol(idRol);
    }
}
