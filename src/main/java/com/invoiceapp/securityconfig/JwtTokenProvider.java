package com.invoiceapp.securityconfig;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.invoiceapp.user.User;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String generateToken(User user){
        String roles = user.getRoles().stream().map(Enum::name).collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .claim("companyId", user.getCompanyId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Long getCompanyId(String token){
        return ((Number) Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody().get("companyId")).longValue();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
