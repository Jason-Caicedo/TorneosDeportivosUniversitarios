package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.mapper;

import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.out.persistence.entity.RolEntity;

/** Mapper Dominio <-> Entidad JPA para Rol. */
public final class RolPersistenceMapper {

    private RolPersistenceMapper() {
    }

    public static Rol toDomain(RolEntity entity) {
        if (entity == null) return null;
        return new Rol(entity.getIdRol(), entity.getNombre(), entity.getDescripcion());
    }

    public static RolEntity toEntity(Rol rol) {
        if (rol == null) return null;
        return new RolEntity(rol.getIdRol(), rol.getNombre(), rol.getDescripcion());
    }
}
