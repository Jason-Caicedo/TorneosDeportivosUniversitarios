package com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.exception;

import com.example.torneosdeportivosuniversitarios.domain.exception.CredencialesInvalidasException;
import com.example.torneosdeportivosuniversitarios.domain.exception.CupoExcedidoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.DisciplinaNoValidaException;
import com.example.torneosdeportivosuniversitarios.domain.exception.DomainException;
import com.example.torneosdeportivosuniversitarios.domain.exception.EstadoTorneoInvalidoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.InscripcionDuplicadaException;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoDuplicadoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoNoEncontradoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.TorneoNoDisponibleException;
import com.example.torneosdeportivosuniversitarios.domain.exception.UsuarioBloqueadoException;
import com.example.torneosdeportivosuniversitarios.infrastructure.adapter.in.rest.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador global de excepciones: traduce las excepciones de negocio (dominio)
 * y de Spring a respuestas HTTP con un formato uniforme (ErrorResponseDTO).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------------- Excepciones de negocio ----------------

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDTO> handleDomain(DomainException ex, HttpServletRequest request) {
        HttpStatus status = statusPara(ex);
        return build(status, ex.getCodigo(), ex.getMessage(), request);
    }

    /** Mapeo excepcion de dominio -> codigo HTTP (el dominio no conoce HTTP). */
    private HttpStatus statusPara(DomainException ex) {
        if (ex instanceof RecursoNoEncontradoException) return HttpStatus.NOT_FOUND;
        if (ex instanceof RecursoDuplicadoException
                || ex instanceof InscripcionDuplicadaException
                || ex instanceof CupoExcedidoException
                || ex instanceof EstadoTorneoInvalidoException
                || ex instanceof TorneoNoDisponibleException) return HttpStatus.CONFLICT;
        if (ex instanceof DisciplinaNoValidaException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof CredencialesInvalidasException) return HttpStatus.UNAUTHORIZED;
        if (ex instanceof UsuarioBloqueadoException) return HttpStatus.FORBIDDEN;
        return HttpStatus.UNPROCESSABLE_ENTITY; // ReglaNegocioException y demas reglas
    }

    // ---------------- Validaciones de entrada ----------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ErrorResponseDTO.of(HttpStatus.BAD_REQUEST,
                "VALIDACION", "Hay campos con errores de validacion", request.getRequestURI(), errores));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotReadable(HttpMessageNotReadableException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "JSON_INVALIDO",
                "El cuerpo de la peticion no es valido (revise el JSON y el formato de fechas yyyy-MM-dd)", request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                               HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "PARAMETRO_INVALIDO",
                "El parametro '" + ex.getName() + "' tiene un valor invalido: " + ex.getValue(), request);
    }

    // ---------------- Seguridad (lanzadas por @PreAuthorize) ----------------

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "ACCESO_DENEGADO",
                "No tiene permisos para realizar esta operacion", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthentication(AuthenticationException ex,
                                                                 HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "NO_AUTENTICADO", ex.getMessage(), request);
    }

    // ---------------- Otros ----------------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                HttpServletRequest request) {
        log.warn("Violacion de integridad: {}", ex.getMostSpecificCause().getMessage());
        return build(HttpStatus.CONFLICT, "INTEGRIDAD_DATOS",
                "La operacion viola una restriccion de la base de datos", request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethod(HttpRequestMethodNotSupportedException ex,
                                                         HttpServletRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "METODO_NO_PERMITIDO", ex.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResource(NoResourceFoundException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "RUTA_NO_ENCONTRADA", "La ruta solicitada no existe", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}", request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO",
                "Ocurrio un error inesperado. Intente mas tarde", request);
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String codigo, String mensaje,
                                                   HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(ErrorResponseDTO.of(status, codigo, mensaje, request.getRequestURI()));
    }
}
