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
    private String jwtSecret;

    // διάρκεια σε milliseconds (π.χ. 86400000 = 24h)
    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** Δημιουργεί JWT για έναν χρήστη με συγκεκριμένο role και ενεργή εταιρεία. */
    public String generateToken(String username, Role role, Long activeCompanyId) {
        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + jwtExpirationMs);

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .claim("act_cid", activeCompanyId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512);

        return builder.compact();
    }

    public String getUsername(String token) {
        return parseClaims(token).map(Claims::getSubject).orElse(null);
    }

    public String getRole(String token) {
        Claims claims = parseClaims(token).orElse(null);
        if (claims == null) return null;
        return (String) claims.get("role");
    }

    public Long getActiveCompanyId(String token) {
        Claims claims = parseClaims(token).orElse(null);
        if (claims == null) return null;
        Object val = claims.get("act_cid");
        return val == null ? null : Long.valueOf(val.toString());
    }

    public boolean validateToken(String token) {
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
