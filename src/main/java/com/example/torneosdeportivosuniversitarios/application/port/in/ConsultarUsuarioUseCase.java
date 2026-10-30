package com.example.torneosdeportivosuniversitarios.application.port.in;

import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;

public interface ConsultarUsuarioUseCase {

    Usuario obtenerPorEmail(String email);
}
