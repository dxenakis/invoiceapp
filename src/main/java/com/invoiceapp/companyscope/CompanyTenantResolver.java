package com.invoiceapp.companyscope;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CompanyTenantResolver implements CurrentTenantIdentifierResolver<Long> {

    // Προτείνω PUBLIC_TENANT = 0L, φρόντισε να μην υπάρχει πραγματική εταιρεία με id=0
    public static final Long PUBLIC_TENANT = 0L;

    @Override
    public Long resolveCurrentTenantIdentifier() {
        Long cid = TenantContext.get();
        return (cid != null) ? cid : PUBLIC_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
