package com.example.torneosdeportivosuniversitarios.infrastructure.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita la cache de Spring (@Cacheable, @CachePut, @CacheEvict en los servicios).
 * Proveedor configurable en application.yaml (spring.cache.type):
 *  - simple : en memoria, no requiere instalar nada (por defecto)
 *  - redis  : perfil "redis" (application-redis.yaml), requiere Redis en localhost:6379
 */
@Configuration
@EnableCaching
public class CacheConfig {
}
