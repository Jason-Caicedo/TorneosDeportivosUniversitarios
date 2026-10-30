package com.example.torneosdeportivosuniversitarios.application.services;

import com.example.torneosdeportivosuniversitarios.application.port.in.AutenticarUsuarioUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.out.PasswordEncoderPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.TokenProviderPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.exception.CredencialesInvalidasException;
import com.example.torneosdeportivosuniversitarios.domain.exception.UsuarioBloqueadoException;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class AutenticarUsuarioService implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public AutenticarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                                    PasswordEncoderPort passwordEncoderPort,
                                    TokenProviderPort tokenProviderPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public ResultadoAutenticacion autenticar(String email, String password) {
        Usuario usuario = usuarioRepositoryPort.findByEmail(email.trim().toLowerCase())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoderPort.matches(password, usuario.getPassword())) {
            throw new CredencialesInvalidasException();
        }
        if (!usuario.estaActivo()) {
            throw new UsuarioBloqueadoException(usuario.getEmail());
        }

        String token = tokenProviderPort.generarToken(usuario);
        return new ResultadoAutenticacion(token, tokenProviderPort.getExpiracionMs(), usuario);
    }
}
