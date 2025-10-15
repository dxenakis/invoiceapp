package com.invoiceapp.companyscope;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.TenantId;

/**
 * Base για ΟΛΑ τα per-company entities.
 * Τα GLOBAL entities (π.χ. Company) ΔΕΝ κληρονομούν αυτό.
 * Όλα τα entities που δεν είναι “παγκόσμια” (global) πρέπει να κάνουν extends CompanyScopedEntity.
 */
@MappedSuperclass
public abstract class CompanyScopedEntity {

    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    protected Long companyId;

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
