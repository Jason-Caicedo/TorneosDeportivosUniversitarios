package com.example.torneosdeportivosuniversitarios.application.port.in;

import com.example.torneosdeportivosuniversitarios.domain.model.Rol;

import java.util.List;

public interface GestionarRolUseCase {

    List<Rol> listar();

    Rol obtener(Long idRol);

    Rol crear(Rol rol);

    Rol actualizar(Long idRol, Rol rol);

    void eliminar(Long idRol);
}
