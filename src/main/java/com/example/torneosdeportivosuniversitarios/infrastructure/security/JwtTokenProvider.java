package com.example.torneosdeportivosuniversitarios.infrastructure.security;

import com.example.torneosdeportivosuniversitarios.application.port.out.TokenProviderPort;
import com.example.torneosdeportivosuniversitarios.domain.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Genera y valida tokens JWT. Implementa el puerto de salida TokenProviderPort.
 * subject = email del usuario; claims extra: rol, username, idUsuario.
 */
@Component
public class JwtTokenProvider implements TokenProviderPort {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey signingKey;
    private final long jwtExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,
                            @Value("${jwt.expiration-ms:86400000}") long jwtExpirationMs) {
        // HS256 exige una clave de al menos 32 bytes (256 bits)
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("rol", usuario.getRol().getNombre())
                .claim("username", usuario.getUsername())
                .claim("idUsuario", usuario.getIdUsuario())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public long getExpiracionMs() {
        return jwtExpirationMs;
    }

    public String getEmailFromToken(String token) {
        return parse(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Token JWT invalido: {}", ex.getMessage());
            return false;
        }
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
