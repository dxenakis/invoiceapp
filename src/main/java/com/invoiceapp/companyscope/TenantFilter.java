package com.invoiceapp.companyscope;

import com.invoiceapp.securityconfig.JwtTokenProvider;
import com.invoiceapp.securityconfig.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE) // να τρέξει ΑΦΟΥ μπει το SecurityContext από το JwtAuthenticationFilter
public class TenantFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;

    public TenantFilter(JwtTokenProvider jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            Long companyId = resolveCompanyId(request);
            if (companyId != null) {
                TenantContext.set(companyId);
            }
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private Long resolveCompanyId(HttpServletRequest req) {
        // 1) Από το SecurityContext (το JwtAuthenticationFilter έχει βάλει ActiveCompanyDetails)
        Long fromContext = SecurityUtils.getActiveCompanyId();
        if (fromContext != null) return fromContext;

        // 2) Από το Authorization: Bearer <jwt> αν υπάρχει (direct parse)
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (jwt.validate(token)) {
                return jwt.getActiveCompanyId(token).orElse(null); // claim name: "act_cid"
            }
        }

        // 3) Τελικό fallback: X-Company-Id (για Postman/manual)
        String header = req.getHeader("X-Company-Id");
        if (header != null && !header.isBlank()) {
            try { return Long.parseLong(header.trim()); } catch (NumberFormatException ignored) {}
        }

        return null;
    }
}
