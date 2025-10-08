package com.invoiceapp.securityconfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.invoiceapp.securityconfig.JwtAuthenticationFilter.ActiveCompanyDetails;
import  com.invoiceapp.access.UserCompanyAccessService;
import com.invoiceapp.user.UserRepository;
import com.invoiceapp.user.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public static Long getCurrentUserIdOrThrow(UserRepository users) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("No authenticated user");
        }
        String username = auth.getName();
        return users.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("Unknown user " + username))
                .getId();
    }

    public static Long getActiveCompanyIdOrThrow() {
        // αν η δική σου μέθοδος επιστρέφει Optional<Long>, κάνε:
        // Long cid = getActiveCompanyId().orElse(null);
        Long cid = getActiveCompanyId();
        if (cid == null) throw new AccessDeniedException("No active company in token");
        return cid;
    }

    public static void assertHasAccessOrNotFound(
            Long companyId,
            UserCompanyAccessService access,
            UserRepository users
    ) {
        Long uid = getCurrentUserIdOrThrow(users);
        if (!access.hasAccess(uid, companyId)) {
            // 404 για να μη διαρρέει η ύπαρξη πόρων άλλης εταιρείας
            throw new EntityNotFoundException("Company not found");
        }
    }

    public static void requireCompanyAdmin(
            Long companyId,
            UserCompanyAccessService access,
            UserRepository users
    ) {
        Long uid = getCurrentUserIdOrThrow(users);
        var roleOpt = access.roleFor(uid, companyId);
        if (roleOpt.isEmpty()) {
            throw new AccessDeniedException("No access to company " + companyId);
        }
        if (roleOpt.get() != Role.COMPANY_ADMIN) {
            throw new AccessDeniedException("Requires COMPANY_ADMIN");
        }
    }

    public static java.util.List<Long> getAccessibleCompanyIds(
            UserCompanyAccessService access,
            UserRepository users
    ) {
        Long uid = getCurrentUserIdOrThrow(users);
        return access.getUserCompanies(uid).stream()
                .map(uca -> uca.getCompanyId())
                .toList();
    }
}