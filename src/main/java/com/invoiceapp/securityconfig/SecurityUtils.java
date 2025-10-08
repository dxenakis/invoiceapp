package com.invoiceapp.securityconfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.invoiceapp.securityconfig.JwtAuthenticationFilter.ActiveCompanyDetails;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static Long getActiveCompanyId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object details = auth.getDetails();
        if (details instanceof ActiveCompanyDetails acd) {
            return acd.getCompanyId();
        }
        return null;
    }

    public static String getRemoteIp() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object details = auth.getDetails();
        if (details instanceof ActiveCompanyDetails acd) {
            return acd.getRemoteAddress();
        }
        return null;
    }

    public static String getSessionId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object details = auth.getDetails();
        if (details instanceof ActiveCompanyDetails acd) {
            return acd.getSessionId();
        }
        return null;
    }
}