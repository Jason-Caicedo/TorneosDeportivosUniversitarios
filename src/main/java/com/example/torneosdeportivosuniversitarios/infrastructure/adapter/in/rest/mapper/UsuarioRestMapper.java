package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.mapper;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.UsuarioRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.UsuarioResponseDTO;

public final class UsuarioRestMapper {

    private UsuarioRestMapper() {
    }

    public static Usuario toDomain(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .build();
    }

    public static UsuarioResponseDTO toResponse(Usuario u) {
        return new UsuarioResponseDTO(
                u.getIdUsuario(),
                u.getUsername(),
                u.getEmail(),
                u.getEstado() == null ? null : u.getEstado().name(),
                u.getRol() == null ? null : u.getRol().getNombre(),
                u.getIdJugador(),
                u.getFechaCreacion());
    }
}
