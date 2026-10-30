package com.example.torneosdeportivosuniversitarios.application.port.in;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;

public interface AutenticarUsuarioUseCase {

    /** Valida credenciales y devuelve el token JWT junto con el usuario autenticado. */
    ResultadoAutenticacion autenticar(String email, String password);

    record ResultadoAutenticacion(String token, long expiraEnMs, Usuario usuario) {
    }
}
