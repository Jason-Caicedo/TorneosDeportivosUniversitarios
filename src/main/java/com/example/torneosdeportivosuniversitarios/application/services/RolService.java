package com.example.torneosdeportivosuniversitarios.application.services;

import com.example.torneosdeportivosuniversitarios.application.port.in.GestionarRolUseCase;
import com.example.torneosdeportivosuniversitarios.application.port.out.RolRepositoryPort;
import com.example.torneosdeportivosuniversitarios.application.port.out.UsuarioRepositoryPort;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoDuplicadoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.RecursoNoEncontradoException;
import com.example.torneosdeportivosuniversitarios.domain.exception.ReglaNegocioException;
import com.example.torneosdeportivosuniversitarios.domain.model.Rol;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CRUD de roles con cache: los roles casi nunca cambian, por eso se cachean.
 * Cache "roles" = listado completo; cache "rol" = rol por id.
 */
@Service
public class RolService implements GestionarRolUseCase {

    private final RolRepositoryPort rolRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public RolService(RolRepositoryPort rolRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort) {
        this.rolRepositoryPort = rolRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    @Cacheable(value = "roles")
    public List<Rol> listar() {
        return rolRepositoryPort.findAll();
    }

    @Override
    @Cacheable(value = "rol", key = "#idRol")
    public Rol obtener(Long idRol) {
        return buscar(idRol);
    }

    @Override
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public Rol crear(Rol rol) {
        String nombre = normalizarNombre(rol.getNombre());
        if (rolRepositoryPort.existsByNombre(nombre)) {
            throw new RecursoDuplicadoException("Ya existe un rol con el nombre: " + nombre);
        }
        rol.setIdRol(null);
        rol.setNombre(nombre);
        return rolRepositoryPort.save(rol);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "roles", allEntries = true),
            @CacheEvict(value = "rol", key = "#idRol")
    })
    public Rol actualizar(Long idRol, Rol cambios) {
        Rol actual = buscar(idRol);
        String nombre = normalizarNombre(cambios.getNombre());
        rolRepositoryPort.findByNombre(nombre)
                .filter(otro -> !otro.getIdRol().equals(idRol))
                .ifPresent(otro -> {
                    throw new RecursoDuplicadoException("Ya existe un rol con el nombre: " + nombre);
                });
        actual.setNombre(nombre);
        actual.setDescripcion(cambios.getDescripcion());
        return rolRepositoryPort.save(actual);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "roles", allEntries = true),
            @CacheEvict(value = "rol", key = "#idRol")
    })
    public void eliminar(Long idRol) {
        buscar(idRol);
        if (usuarioRepositoryPort.existsByRol(idRol)) {
            throw new ReglaNegocioException("No se puede eliminar el rol porque tiene usuarios asignados");
        }
        rolRepositoryPort.deleteById(idRol);
    }

    private Rol buscar(Long idRol) {
        return rolRepositoryPort.findById(idRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", idRol));
    }

    /** Los roles deben seguir la convencion ROLE_XXXX (necesaria para hasRole de Spring Security). */
    private String normalizarNombre(String nombre) {
        String n = nombre.trim().toUpperCase().replace(' ', '_');
        return n.startsWith("ROLE_") ? n : "ROLE_" + n;
    }
}
