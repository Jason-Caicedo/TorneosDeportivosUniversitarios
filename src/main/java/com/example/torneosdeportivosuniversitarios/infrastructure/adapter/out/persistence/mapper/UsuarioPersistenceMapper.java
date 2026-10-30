package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.mapper;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoUsuario;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity.UsuarioEntity;

/** Mapper Dominio <-> Entidad JPA para Usuario. */
public final class UsuarioPersistenceMapper {

    private UsuarioPersistenceMapper() {
    }

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        return Usuario.builder()
                .idUsuario(entity.getIdUsuario())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .estado(entity.getEstado() == null ? null : EstadoUsuario.valueOf(entity.getEstado()))
                .rol(RolPersistenceMapper.toDomain(entity.getRol()))
                .idJugador(entity.getIdJugador())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /** El rol se asigna en el adaptador (necesita la referencia JPA administrada). */
    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setUsername(usuario.getUsername());
        entity.setEmail(usuario.getEmail());
        entity.setPassword(usuario.getPassword());
        entity.setEstado(usuario.getEstado() == null ? null : usuario.getEstado().name());
        entity.setIdJugador(usuario.getIdJugador());
        entity.setFechaCreacion(usuario.getFechaCreacion());
        return entity;
    }
}
