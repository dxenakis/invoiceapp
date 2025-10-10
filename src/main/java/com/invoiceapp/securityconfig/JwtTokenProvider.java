package com.invoiceapp.securityconfig;

import com.invoiceapp.user.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key}")
    private String jwtSecret; // RAW string (βάλε >=32 ascii chars)

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationMs; // σε ms

    private Key getSigningKey() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT secret (security.jwt.secret-key) is not configured.");
        }
        byte[] keyBytes = jwtSecret.trim().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) { // HS256 => >= 256 bits
            throw new IllegalStateException("JWT secret must be at least 32 bytes (add a longer value to security.jwt.secret-key).");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** Token ΧΩΡΙΣ active company (μετά από register ή πριν κάνει switch). */
    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Token ΜΕ ρόλο & ενεργή εταιρεία. */
    public String generateToken(String username, Role role, Long activeCompanyId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role != null ? role.name() : null)
                .claim("act_cid", activeCompanyId)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<String> getUsername(String token) {
        return parseClaims(token).map(Claims::getSubject);
    }

    public Optional<Role> getRole(String token) {
        return parseClaims(token)
                .map(c -> c.get("role", String.class))
                .map(s -> s == null ? null : Role.valueOf(s));
    }

    public Optional<Long> getActiveCompanyId(String token) {
        return parseClaims(token)
                .map(c -> c.get("act_cid"))
                .map(v -> v == null ? null : Long.valueOf(String.valueOf(v)));
    }

    private Optional<Claims> parseClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
