package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest;

import com.example.torneosdeportivosuniversitarios.application.port.in.ActualizarTorneoUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.in.ConsultarTorneoUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.in.CrearTorneoUseCase;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.ErrorResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.TorneoRequestDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.TorneoResponseDTO;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.mapper.TorneoRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

/**
 * Torneos:
 *  - Consultas: cualquier usuario autenticado.
 *  - Crear / actualizar / iniciar / cerrar: solo ROLE_ADMIN (regla 9).
 */
@RestController
@RequestMapping("/api/torneos")
@Tag(name = "Torneos", description = "Gestion de torneos deportivos")
@ApiResponse(responseCode = "401", description = "Token ausente o invalido",
        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
public class TorneoController {

    private final CrearTorneoUseCase crearTorneoUseCase;
    private final ConsultarTorneoUseCase consultarTorneoUseCase;
    private final ActualizarTorneoUseCase actualizarTorneoUseCase;

    public TorneoController(CrearTorneoUseCase crearTorneoUseCase,
                            ConsultarTorneoUseCase consultarTorneoUseCase,
                            ActualizarTorneoUseCase actualizarTorneoUseCase) {
        this.crearTorneoUseCase = crearTorneoUseCase;
        this.consultarTorneoUseCase = consultarTorneoUseCase;
        this.actualizarTorneoUseCase = actualizarTorneoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear torneo (ADMIN)", description = "Se crea en estado PLANIFICADO. Valida la disciplina")
    @ApiResponse(responseCode = "201", description = "Torneo creado")
    @ApiResponse(responseCode = "400", description = "Datos o disciplina invalidos",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "403", description = "Requiere ROLE_ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "422", description = "Regla de negocio (ej: fecha fin <= fecha inicio)",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TorneoResponseDTO> crear(@Valid @RequestBody TorneoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TorneoRestMapper.toResponse(crearTorneoUseCase.crear(TorneoRestMapper.toDomain(request))));
    }

    @GetMapping
    @Operation(summary = "Listar torneos", description = "Filtros opcionales por disciplina y estado")
    @ApiResponse(responseCode = "200", description = "Listado de torneos")
    public ResponseEntity<List<TorneoResponseDTO>> listar(
            @Parameter(description = "Ej: Fútbol, Voleibol, Baloncesto") @RequestParam(required = false) String disciplina,
            @Parameter(description = "PLANIFICADO, EN_CURSO o FINALIZADO") @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(consultarTorneoUseCase.listar(disciplina, estado).stream()
                .map(TorneoRestMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener torneo por id")
    @ApiResponse(responseCode = "200", description = "Torneo encontrado")
    @ApiResponse(responseCode = "404", description = "Torneo no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TorneoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(TorneoRestMapper.toResponse(consultarTorneoUseCase.obtener(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar torneo (ADMIN)", description = "No se permite si el torneo esta FINALIZADO")
    @ApiResponse(responseCode = "200", description = "Torneo actualizado")
    @ApiResponse(responseCode = "403", description = "Requiere ROLE_ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Torneo no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "409", description = "Torneo FINALIZADO",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TorneoResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody TorneoRequestDTO request) {
        return ResponseEntity.ok(TorneoRestMapper.toResponse(
                actualizarTorneoUseCase.actualizar(id, TorneoRestMapper.toDomain(request))));
    }

    @PutMapping("/{id}/iniciar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Iniciar torneo (ADMIN)", description = "Cambia el estado de PLANIFICADO a EN_CURSO")
    @ApiResponse(responseCode = "200", description = "Torneo EN_CURSO")
    @ApiResponse(responseCode = "403", description = "Requiere ROLE_ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "409", description = "El torneo no esta PLANIFICADO",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TorneoResponseDTO> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(TorneoRestMapper.toResponse(actualizarTorneoUseCase.iniciar(id)));
    }

    @PutMapping("/{id}/cerrar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cerrar torneo (ADMIN)", description = "Cambia el estado de EN_CURSO a FINALIZADO")
    @ApiResponse(responseCode = "200", description = "Torneo FINALIZADO")
    @ApiResponse(responseCode = "403", description = "Requiere ROLE_ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "409", description = "El torneo no esta EN_CURSO",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TorneoResponseDTO> cerrar(@PathVariable Long id) {
        return ResponseEntity.ok(TorneoRestMapper.toResponse(actualizarTorneoUseCase.cerrar(id)));
    }
}
