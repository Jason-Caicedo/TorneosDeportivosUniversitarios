# Primer avance – Torneos Deportivos Universitarios

## Cómo ejecutarlo
1. Abrir la carpeta en IntelliJ IDEA (File > Open > seleccionar la carpeta que contiene `pom.xml`).
2. Esperar a que Maven descargue las dependencias (icono "Reload Maven project" si no lo hace solo).
3. JDK 17 o superior (File > Project Structure > SDK).
4. Ejecutar `TorneosDeportivosUniversitariosApplication`.
5. Abrir **http://localhost:8080/swagger-ui.html**

Al iniciar, la app crea automáticamente (si no existen) los roles `ROLE_ADMIN`, `ROLE_ARBITRO`,
`ROLE_ESTUDIANTE` y un usuario administrador:

| Email | Password |
|---|---|
| admin@uniajc.edu.co | Admin123* |

(Se configura en `application.yaml` → `app.admin`).

## Cómo probar en Swagger
1. `POST /api/auth/login` con el admin → copiar el `token`.
2. Botón **Authorize** (candado) → pegar SOLO el token → Authorize.
3. Probar `POST /api/torneos`, `PUT /api/torneos/{id}/iniciar`, `GET /api/roles`, etc.
4. Para probar el 403: registrar un estudiante en `POST /api/auth/registro`, hacer login con él,
   autorizar con su token e intentar `POST /api/torneos` → **403 ACCESO_DENEGADO**.

También está `api-pruebas.http` para ejecutar las peticiones desde IntelliJ.

## Qué se cumple del primer avance

| Requisito | Dónde está |
|---|---|
| Arquitectura hexagonal | `domain` (modelos, enums, excepciones, sin Spring/JPA) → `application` (puertos `in`/`out` + servicios) → `infrastructure` (adaptadores REST, JPA, seguridad, config). La aplicación no depende de Spring Security: usa los puertos `PasswordEncoderPort` y `TokenProviderPort`. |
| Módulo de seguridad | `infrastructure/security`: `SecurityConfig`, `JwtTokenProvider`, `JwtAuthenticationFilter`, `CustomUserDetailsService`, `BCryptPasswordEncoderAdapter`, `RestSecurityErrorHandler` (401/403 en JSON). Endpoints `/api/auth/registro`, `/api/auth/login`, `/api/auth/me`. |
| Seguridad aplicada a un controlador | `TorneoController`: consultas para cualquier autenticado; crear/actualizar/iniciar/cerrar con `@PreAuthorize("hasRole('ADMIN')")`. `RolController`: todo el controlador es solo ADMIN. Todo fuera de `/api/auth/**` exige JWT (regla 12). |
| Caché | `CacheConfig` (`@EnableCaching`). `TorneoService`: `@Cacheable` en listar/obtener, `@CachePut` + `@CacheEvict` en crear/actualizar/iniciar/cerrar. `RolService`: `@Cacheable`/`@CacheEvict`. Por defecto en memoria (`spring.cache.type: simple`); con el perfil `redis` usa Redis. |
| Excepciones de negocio + controlador de excepciones | `domain/exception` (`DomainException`, `RecursoNoEncontradoException`, `DisciplinaNoValidaException`, `EstadoTorneoInvalidoException`, `CupoExcedidoException`, `InscripcionDuplicadaException`, `TorneoNoDisponibleException`, …) y `GlobalExceptionHandler` (`@RestControllerAdvice`) que responde con `ErrorResponseDTO`. |
| Swagger | `OpenApiConfig` (esquema Bearer JWT), `@Tag`, `@Operation`, `@ApiResponse`, `@Schema` en controladores y DTOs. |

Reglas de negocio ya implementadas: 7 (disciplina validada en código), 8 (sin password en respuestas),
9 (ADMIN gestiona torneos), 12 (JWT obligatorio), transición de estados del torneo
(PLANIFICADO → EN_CURSO → FINALIZADO) y fecha fin > fecha inicio.

## Códigos de error
| HTTP | Cuándo |
|---|---|
| 400 | Validación de campos, JSON inválido, disciplina no permitida |
| 401 | Sin token / token inválido / credenciales incorrectas |
| 403 | Rol sin permiso / usuario bloqueado |
| 404 | Recurso no existe |
| 409 | Duplicados, transición de estado no permitida |
| 422 | Otra regla de negocio (ej. fechas) |

## Redis (opcional)
`docker run -d -p 6379:6379 redis` y en IntelliJ: Run > Edit Configurations > Active profiles: `redis`.

## Pruebas automáticas
`mvn test` ejecuta `PrimerAvanceIntegrationTest` con base de datos H2 en memoria
(no toca la base de datos de Neon).
