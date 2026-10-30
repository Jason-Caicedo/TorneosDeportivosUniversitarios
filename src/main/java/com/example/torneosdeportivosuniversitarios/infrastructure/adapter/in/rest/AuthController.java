package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest;

import com.example.torneosdeportivosuniversitarios.application.port.in.AutenticarUsuarioUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.in.AutenticarUsuarioUseCase.ResultadoAutenticacion;
import com.example.torneosdeportivosuniversitarios.application.port.in.ConsultarUsuarioUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.in.RegistrarUsuarioUseCase;
import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.ErrorResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.JwtResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.LoginRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.UsuarioRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.UsuarioResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.mapper.UsuarioRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Registro, login (JWT) y datos del usuario autenticado")
public class AuthController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;

    public AuthController(RegistrarUsuarioUseCase registrarUsuarioUseCase,
                          AutenticarUsuarioUseCase autenticarUsuarioUseCase,
                          ConsultarUsuarioUseCase consultarUsuarioUseCase) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.consultarUsuarioUseCase = consultarUsuarioUseCase;
    }

    @PostMapping("/registro")
    @SecurityRequirements // endpoint publico: sin candado en Swagger
    @Operation(summary = "Registrar usuario",
            description = "Crea un usuario con password encriptado (BCrypt) y rol ROLE_ESTUDIANTE. Publico.")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "409", description = "Email o username ya registrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario registrado = registrarUsuarioUseCase.registrar(UsuarioRestMapper.toDomain(request), Rol.ESTUDIANTE);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioRestMapper.toResponse(registrado));
    }

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Iniciar sesion",
            description = "Autentica con email y password y devuelve un token JWT. Publico.")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales invalidas",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "403", description = "Usuario bloqueado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        ResultadoAutenticacion r = autenticarUsuarioUseCase.autenticar(request.email(), request.password());
        return ResponseEntity.ok(new JwtResponseDTO(r.token(), "Bearer", r.expiraEnMs(),
                UsuarioRestMapper.toResponse(r.usuario())));
    }

    @GetMapping("/me")
    @Operation(summary = "Usuario autenticado", description = "Devuelve los datos del usuario dueño del token")
    @ApiResponse(responseCode = "200", description = "Datos del usuario")
    @ApiResponse(responseCode = "401", description = "Token ausente o invalido",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        Usuario usuario = consultarUsuarioUseCase.obtenerPorEmail(authentication.getName());
        return ResponseEntity.ok(UsuarioRestMapper.toResponse(usuario));
    }
}
