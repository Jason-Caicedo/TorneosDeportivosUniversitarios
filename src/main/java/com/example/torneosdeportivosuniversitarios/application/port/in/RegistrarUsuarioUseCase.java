package com.example.torneosdeportivosuniversitarios.application.port.in;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;

public interface RegistrarUsuarioUseCase {

    /** Registra un usuario con el rol indicado, encriptando su password. */
    Usuario registrar(Usuario usuario, String nombreRol);
}
