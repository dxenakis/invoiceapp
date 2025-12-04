package com.invoiceapp.securityconfig;

import com.invoiceapp.user.GlobalRole;
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

    @Value("${security.jwt.expiration-time}")          // ACCESS TTL (ms), π.χ. 900000 = 15'
    private long accessExpirationMs;

    @Value("${security.jwt.refresh-expiration-ms}")    // REFRESH TTL (ms), π.χ. 2592000000 = 30d
    private long refreshExpirationMs;

    @Value("${security.jwt.issuer:invoiceapp}")
    private String issuer;

    @Value("${security.jwt.access-audience:api}")
    private String accessAudience;

    @Value("${security.jwt.refresh-audience:refresh}")
    private String refreshAudience;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.trim().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("security.jwt.secret-key must be at least 32 bytes.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** Ένα API: αν actCid == null → pre-tenant access (χωρίς claim), αλλιώς tenant-bound. */
    public String generateAccessToken(String username, GlobalRole globalRole, Role role, Long actCid) {
        long now = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setAudience(accessAudience)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        if (role != null) {
            builder.claim("role", role.name());
        }
        if (actCid != null) {
            builder.claim("act_cid", actCid);
        }

        if (globalRole != null) {
            builder.claim("g_role", globalRole.name());
        }

        return builder.compact();
    }

    /** ver = users.refreshVersion τη στιγμή έκδοσης */
    public String generateRefreshToken(String username, int version) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setAudience(refreshAudience)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshExpirationMs))
                .claim("ver", version)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ===== Parsing (κοινό) ===== */
    private Optional<Claims> parseClaims(String token) {
        try {
            Claims c = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(c);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /* ===== Validation ===== */
    /** ΚΡΑΤΑΜΕ ΤΟ ΟΝΟΜΑ ΩΣ ΕΧΕΙ — χρησιμοποιείται από το filter για ACCESS */
    public boolean validate(String token) {
        return parseClaims(token)
                .filter(c -> issuer.equals(c.getIssuer()))
                .filter(c -> accessAudience.equals(c.getAudience()))
                .isPresent();
    }

    public boolean validateRefresh(String token) {
        return parseClaims(token)
                .filter(c -> issuer.equals(c.getIssuer()))
                .filter(c -> refreshAudience.equals(c.getAudience()))
                .isPresent();
    }

    /* ===== Helpers ===== */
    public Optional<String> getUsername(String token) {
        return parseClaims(token).map(Claims::getSubject);
    }

    public Optional<Role> getRole(String token) {
        return parseClaims(token)
                .map(c -> c.get("role", String.class))
                .map(s -> s == null ? null : Role.valueOf(s));
    }

    public Optional<GlobalRole> getGlobalRole(String token) {
        return parseClaims(token)
                .map(c -> c.get("g_role", String.class))
                .map(s -> s == null ? null : GlobalRole.valueOf(s));
    }

    public Optional<Long> getActiveCompanyId(String token) {
        return parseClaims(token).map(c -> c.get("act_cid", Long.class));
    }

    public Optional<Integer> getRefreshVersion(String token) {
        return parseClaims(token).map(c -> c.get("ver", Integer.class));
    }
}
