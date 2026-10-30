package com.example.torneosdeportivosuniversitarios.application.port.out;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;

/** Puerto de salida para generar el token de acceso (implementado con JWT). */
public interface TokenProviderPort {

    String generarToken(Usuario usuario);

    long getExpiracionMs();
}
