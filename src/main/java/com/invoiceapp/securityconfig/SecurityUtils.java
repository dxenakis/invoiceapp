package com.invoiceapp.securityconfig;

import com.invoiceapp.access.UserCompanyAccess;
import com.invoiceapp.access.UserCompanyAccessService;
import com.invoiceapp.user.Role;
import com.invoiceapp.user.User;
import com.invoiceapp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static com.invoiceapp.securityconfig.JwtAuthenticationFilter.ActiveCompanyDetails;

/**
 * Βοηθητικές μέθοδοι ασφάλειας για user/company context.
 */
public final class SecurityUtils {
    private SecurityUtils() {}

    /** Username από SecurityContext ή null αν δεν υπάρχει. */
    public static String getCurrentUsernameOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? String.valueOf(auth.getPrincipal()) : null;
    }

    /** Επιστρέφει το ID του τρέχοντος χρήστη ή 403 αν λείπει/δεν βρεθεί. */
    public static Long getCurrentUserIdOrThrow(UserRepository users) {
        String username = getCurrentUsernameOrNull();
        if (username == null) throw new AccessDeniedException("Unauthenticated");
        User u = users.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
        return u.getId();
    }

    /** Active company id από το auth.details (μπορεί να είναι null). */
    public static Long getActiveCompanyId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object details = auth.getDetails();
        if (details instanceof ActiveCompanyDetails acd) return acd.getCompanyId();
        return null;
    }

    /** Επιστρέφει true αν ο χρήστης έχει ΚΑΠΟΙΑ πρόσβαση στην εταιρεία. */
    public static boolean hasAccess(Long userId, Long companyId, UserCompanyAccessService access) {
        return access.roleFor(userId, companyId).isPresent();
    }

    /**
     * Αν ο χρήστης δεν έχει πρόσβαση, ρίχνει 404 (obfuscation: "Company not found").
     * Αν έχει, απλά επιστρέφει.
     */
    public static void assertHasAccessOrNotFound(Long companyId,
                                                 UserCompanyAccessService access,
                                                 UserRepository users) {
        Long userId = getCurrentUserIdOrThrow(users);
        if (!hasAccess(userId, companyId, access)) {
            throw new EntityNotFoundException("Company not found");
        }
    }

    /**
     * Επιστρέφει όλα τα companyIds στα οποία έχει πρόσβαση ο τρέχων χρήστης.
     */
    public static List<Long> getAccessibleCompanyIds(UserCompanyAccessService access,
                                                     UserRepository users) {
        Long userId = getCurrentUserIdOrThrow(users);
        List<UserCompanyAccess> list = access.getUserCompanies(userId);
        return list.stream().map(UserCompanyAccess::getCompanyId).collect(Collectors.toList());
    }

    /**
     * Απαιτεί ο χρήστης να είναι COMPANY_ADMIN στη συγκεκριμένη εταιρεία.
     * Αν δεν είναι/δεν έχει πρόσβαση => 403.
     */
    public static void requireCompanyAdmin(Long companyId,
                                           UserCompanyAccessService access,
                                           UserRepository users) {
        Long userId = getCurrentUserIdOrThrow(users);
        var roleOpt = access.roleFor(userId, companyId);
        if (roleOpt.isEmpty()) {
            throw new AccessDeniedException("No access to this company");
        }
        if (roleOpt.get() != Role.COMPANY_ADMIN) {
            throw new AccessDeniedException("Requires COMPANY_ADMIN");
        }
    }
    /** Επιστρέφει το activeCompanyId ή ρίχνει IllegalStateException αν λείπει context. */
    public static Long getActiveCompanyIdOrThrow() {
        Long cid = getActiveCompanyId();
        if (cid == null) {
            throw new IllegalStateException("Missing active company context");
        }
        return cid;
    }

    /**
     * Βοηθητικό για endpoints που απαιτούν active company:
     * επιστρέφει το id ή ρίχνει IllegalArgumentException (ώστε να γίνει 400/422 από GlobalExceptionHandler).
     */
    public static Long requireActiveCompanyContext() {
        Long cid = getActiveCompanyId();
        if (cid == null) {
            throw new IllegalArgumentException("Active company is required for this operation");
        }
        return cid;
    }
}
