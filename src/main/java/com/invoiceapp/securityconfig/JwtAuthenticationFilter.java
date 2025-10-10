package com.invoiceapp.securityconfig;

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

            if (jwt.validate(token)) {
                String username = jwt.getUsername(token).orElse(null);
                var roleOpt = jwt.getRole(token);
                var companyIdOpt = jwt.getActiveCompanyId(token);

                if (username != null) {
                    var authorities = roleOpt
                            .map(r -> List.of(new SimpleGrantedAuthority("ROLE_" + r.name())))
                            .orElse(List.of(new SimpleGrantedAuthority("ROLE_USER"))); // default

                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    auth.setDetails(new ActiveCompanyDetails(req, companyIdOpt.orElse(null)));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        chain.doFilter(req, res);
    }

    /** Κουβαλάμε το act_cid μέσα στο SecurityContext */
    public static class ActiveCompanyDetails extends WebAuthenticationDetails {
        private final Long companyId;

        public ActiveCompanyDetails(HttpServletRequest request, Long companyId) {
            super(request);
            this.companyId = companyId;
        }

        public Long getCompanyId() {
            return companyId;
        }
    }
}
