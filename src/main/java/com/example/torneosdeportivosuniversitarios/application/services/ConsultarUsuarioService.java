package com.example.torneosdeportivosuniversitarios.application.services;

import com.example.torneosdeportivosuniversitarios.application.port.in.ConsultarUsuarioUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoNoEncontradoException;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class ConsultarUsuarioService implements ConsultarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ConsultarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", email));
    }
}
