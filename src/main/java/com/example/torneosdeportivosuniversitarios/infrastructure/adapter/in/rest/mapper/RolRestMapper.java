package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.mapper;

import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.RolRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.RolResponseDTO;

public final class RolRestMapper {

    private RolRestMapper() {
    }

    public static Rol toDomain(RolRequestDTO dto) {
        return Rol.builder().nombre(dto.nombre()).descripcion(dto.descripcion()).build();
    }

    public static RolResponseDTO toResponse(Rol rol) {
        return new RolResponseDTO(rol.getIdRol(), rol.getNombre(), rol.getDescripcion());
    }
}
