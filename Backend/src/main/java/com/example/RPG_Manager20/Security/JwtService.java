package com.example.RPG_Manager20.Security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;   // ← ESSE
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret:minha-chave-super-secreta-com-pelo-menos-32-caracteres-aqui}") String secret,
                      @Value("${jwt.expiration:86400000}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }
    public String gerarToken(Long idUsuario, String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("usuarioId", idUsuario)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims obterToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean tokenValido(String token) {
        try {
            obterToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String getEmail(String token) {
        return obterToken(token).getSubject();
    }

    public Long getUsuarioId(String token) {
        return obterToken(token).get("usuarioId", Long.class);
    }

}
