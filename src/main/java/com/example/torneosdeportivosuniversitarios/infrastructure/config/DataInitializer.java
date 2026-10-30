package com.example.torneosdeportivosuniversitarios.infrastructure.config;

import com.example.torneosdeportivosuniversitarios.application.port.out.PasswordEncoderPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.RolRepositoryPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import com.example.torneosdeportivosuniversitarios.domain.model.enums.EstadoUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Al iniciar la aplicacion crea (si no existen) los 3 roles base y un usuario ADMIN,
 * para poder probar los endpoints protegidos sin tocar la base de datos a mano.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RolRepositoryPort rolRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Value("${app.admin.username:admin}")
    private String adminUsername;
    @Value("${app.admin.email:admin@uniajc.edu.co}")
    private String adminEmail;
    @Value("${app.admin.password:Admin123*}")
    private String adminPassword;

    public DataInitializer(RolRepositoryPort rolRepositoryPort,
                           UsuarioRepositoryPort usuarioRepositoryPort,
                           PasswordEncoderPort passwordEncoderPort) {
        this.rolRepositoryPort = rolRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map.of(
                Rol.ADMIN, "Administrador del sistema",
                Rol.ARBITRO, "Árbitro de partidos",
                Rol.ESTUDIANTE, "Estudiante/jugador"
        ).forEach((nombre, descripcion) -> {
            if (!rolRepositoryPort.existsByNombre(nombre)) {
                rolRepositoryPort.save(Rol.builder().nombre(nombre).descripcion(descripcion).build());
                log.info("Rol creado: {}", nombre);
            }
        });

        if (!usuarioRepositoryPort.existsByEmail(adminEmail) && !usuarioRepositoryPort.existsByUsername(adminUsername)) {
            Rol rolAdmin = rolRepositoryPort.findByNombre(Rol.ADMIN).orElseThrow();
            usuarioRepositoryPort.save(Usuario.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoderPort.encode(adminPassword))
                    .estado(EstadoUsuario.ACTIVO)
                    .rol(rolAdmin)
                    .fechaCreacion(LocalDateTime.now())
                    .build());
            log.info("Usuario ADMIN creado: {} / {}", adminEmail, adminPassword);
        }
    }
}
