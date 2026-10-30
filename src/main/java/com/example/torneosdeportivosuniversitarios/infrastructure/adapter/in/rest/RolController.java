package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest;

import com.example.torneosdeportivosuniversitarios.application.port.in.GestionarRolUseCase;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.ErrorResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.RolRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.RolResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.mapper.RolRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** CRUD de roles. TODO el controlador exige ROLE_ADMIN (seguridad a nivel de clase). */
@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Roles", description = "CRUD de roles. Acceso exclusivo ADMIN")
@ApiResponse(responseCode = "401", description = "Token ausente o invalido",
        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
@ApiResponse(responseCode = "403", description = "Requiere ROLE_ADMIN",
        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
public class RolController {

    private final GestionarRolUseCase gestionarRolUseCase;

    public RolController(GestionarRolUseCase gestionarRolUseCase) {
        this.gestionarRolUseCase = gestionarRolUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar roles")
    @ApiResponse(responseCode = "200", description = "Listado de roles")
    public ResponseEntity<List<RolResponseDTO>> listar() {
        return ResponseEntity.ok(gestionarRolUseCase.listar().stream().map(RolRestMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por id")
    @ApiResponse(responseCode = "200", description = "Rol encontrado")
    @ApiResponse(responseCode = "404", description = "Rol no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<RolResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(RolRestMapper.toResponse(gestionarRolUseCase.obtener(id)));
    }

    @PostMapping
    @Operation(summary = "Crear rol")
    @ApiResponse(responseCode = "201", description = "Rol creado")
    @ApiResponse(responseCode = "409", description = "Nombre duplicado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<RolResponseDTO> crear(@Valid @RequestBody RolRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RolRestMapper.toResponse(gestionarRolUseCase.crear(RolRestMapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol")
    @ApiResponse(responseCode = "200", description = "Rol actualizado")
    @ApiResponse(responseCode = "404", description = "Rol no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<RolResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RolRequestDTO request) {
        return ResponseEntity.ok(RolRestMapper.toResponse(
                gestionarRolUseCase.actualizar(id, RolRestMapper.toDomain(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol", description = "No se puede eliminar un rol con usuarios asignados")
    @ApiResponse(responseCode = "204", description = "Rol eliminado")
    @ApiResponse(responseCode = "422", description = "El rol tiene usuarios asignados",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionarRolUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
