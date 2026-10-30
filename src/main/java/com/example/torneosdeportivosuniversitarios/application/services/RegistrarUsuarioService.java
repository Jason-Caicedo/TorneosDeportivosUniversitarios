package com.example.torneosdeportivosuniversitarios.application.services;

import com.example.torneosdeportivosuniversitarios.application.port.in.RegistrarUsuarioUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.out.PasswordEncoderPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.RolRepositoryPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoDuplicadoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoNoEncontradoException;
import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrarUsuarioService implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final RolRepositoryPort rolRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public RegistrarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                                   RolRepositoryPort rolRepositoryPort,
                                   PasswordEncoderPort passwordEncoderPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.rolRepositoryPort = rolRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    @Transactional
    public Usuario registrar(Usuario usuario, String nombreRol) {
        String email = usuario.getEmail().trim().toLowerCase();
        String username = usuario.getUsername().trim();

        if (usuarioRepositoryPort.existsByEmail(email)) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el email: " + email);
        }
        if (usuarioRepositoryPort.existsByUsername(username)) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el username: " + username);
        }

        Rol rol = rolRepositoryPort.findByNombre(nombreRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", nombreRol));

        usuario.setIdUsuario(null);
        usuario.setEmail(email);
        usuario.setUsername(username);
        // Regla de seguridad: el password se guarda con hash BCrypt
        usuario.setPassword(passwordEncoderPort.encode(usuario.getPassword()));
        usuario.setRol(rol);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setFechaCreacion(LocalDateTime.now());

        return usuarioRepositoryPort.save(usuario);
    }
}
