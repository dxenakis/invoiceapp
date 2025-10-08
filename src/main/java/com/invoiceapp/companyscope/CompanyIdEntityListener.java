package com.invoiceapp.companyscope;

import com.invoiceapp.securityconfig.SecurityUtils;
import jakarta.persistence.PrePersist;
import org.springframework.security.access.AccessDeniedException;

/** Γεμίζει αυτόματα το company_id πριν το INSERT για per-company entities. */
public class CompanyIdEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof CompanyScopedEntity cse) {
            Long cid = SecurityUtils.getActiveCompanyId();
            if (cid == null) throw new AccessDeniedException("No active company in token");
            cse.setCompanyId(cid);
        }
    }
}
