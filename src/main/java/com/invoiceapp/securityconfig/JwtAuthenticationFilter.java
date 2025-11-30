package com.invoiceapp.securityconfig;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;

    public JwtAuthenticationFilter(JwtTokenProvider jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // Αν το token είναι έγκυρο, βάζουμε Authentication και συνεχίζουμε
                if (jwt.validate(token)) {
                    String username = jwt.getUsername(token).orElse(null);
                    var roleOpt = jwt.getRole(token);
                    var companyIdOpt = jwt.getActiveCompanyId(token);

                    if (username != null) {
                        var authorities = roleOpt
                                .map(r -> List.of(new SimpleGrantedAuthority("ROLE_" + r.name())))
                                .orElse(List.of(new SimpleGrantedAuthority("ROLE_USER")));
                        var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        auth.setDetails(new ActiveCompanyDetails(req, companyIdOpt.orElse(null)));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } else {
                    // validate επέστρεψε false -> malformed/invalid token
                    sendUnauthorized(res, "INVALID_TOKEN", "Invalid access token", false);
                    return;
                }
            } catch (ExpiredJwtException ex) {
                // token έχει λήξει -> 401 + flag
                sendUnauthorized(res, "TOKEN_EXPIRED", "Access token expired", true);
                return;
            } catch (JwtException | IllegalArgumentException ex) {
                // άλλη JWT εξαίρεση -> invalid token
                sendUnauthorized(res, "INVALID_TOKEN", "Invalid access token", false);
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private void sendUnauthorized(HttpServletResponse res, String code, String message, boolean expired) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (expired) {
            res.setHeader("X-Token-Expired", "1");
        }
        res.setContentType("application/json;charset=UTF-8");
        String body = String.format("{\"code\":\"%s\",\"message\":\"%s\"}", code, message);
        res.getWriter().write(body);
    }

    public static class ActiveCompanyDetails extends WebAuthenticationDetails {
        private final Long companyId;
        public ActiveCompanyDetails(HttpServletRequest request, Long companyId) {
            super(request);
            this.companyId = companyId;
        }
        public Long getCompanyId() { return companyId; }
    }
}
