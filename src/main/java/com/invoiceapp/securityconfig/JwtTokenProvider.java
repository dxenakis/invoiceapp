package com.invoiceapp.securityconfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.invoiceapp.user.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

@Component
public class JwtTokenProvider {

    // Βάλε αυτό το secret σε env var / secrets manager. Για HS512 θέλεις μεγάλο key (π.χ. 64 bytes).
    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    // Σε milliseconds
    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        String roles = Optional.ofNullable(user.getRoles())
                .map(rs -> rs.stream().map(Enum::name).collect(Collectors.joining(",")))
                .orElse("");

        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .claim("companyId", user.getCompanyId())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsername(String token) {
        return parseClaims(token).map(Claims::getSubject).orElse(null);
    }

    public Long getCompanyId(String token) {
        Claims claims = parseClaims(token).orElse(null);
        if (claims == null) return null;
        Object val = claims.get("companyId");
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).longValue();
        try {
            return Long.valueOf(String.valueOf(val));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Set<String> getRoles(String token) {
        Claims claims = parseClaims(token).orElse(null);
        if (claims == null) return Set.of();
        Object rolesObj = claims.get("roles");
        if (rolesObj == null) return Set.of();
        String rolesStr = String.valueOf(rolesObj);
        if (rolesStr.isBlank()) return Set.of();
        return Arrays.stream(rolesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // token expired — μπορείς να το λογκάρεις ξεχωριστά αν θες
        } catch (JwtException | IllegalArgumentException e) {
            // invalid token
        }
        return false;
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
