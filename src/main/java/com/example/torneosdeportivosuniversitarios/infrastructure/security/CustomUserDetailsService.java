package com.example.torneosdeportivosuniversitarios.infrastructure.security;

import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/** Carga el usuario (por email) y su rol para Spring Security. */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public CustomUserDetailsService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(usuario.getRol().getNombre())))
                .disabled(!usuario.estaActivo())
                .build();
    }
}
