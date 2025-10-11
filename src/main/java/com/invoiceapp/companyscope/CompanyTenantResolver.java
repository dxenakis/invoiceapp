package com.invoiceapp.companyscope;

import com.invoiceapp.securityconfig.SecurityUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * Resolver για Hibernate 6 DISCRIMINATOR multi-tenancy.
 * Δίνει ΠΑΝΤΑ tenant id:
 *  - από JWT (αν υπάρχει)
 *  - αλλιώς DEFAULT_TENANT (-1L) για bootstrap/background.
 */
@Component
public class CompanyTenantResolver implements CurrentTenantIdentifierResolver<Long> {

    /** Ειδική τιμή που δεν αντιστοιχεί σε κανονικό tenant. */
    public static final Long DEFAULT_TENANT = -1L;

    @Override
    public Long resolveCurrentTenantIdentifier() {
        Long cid = SecurityUtils.getActiveCompanyId();
        return cid != null ? cid : DEFAULT_TENANT;
    }

    /**
     * true: επιτρέπει re-use sessions ακόμη και αν αλλάξει tenant στον ίδιο thread.
     * Για web apps είναι ασφαλές να μείνει true.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
