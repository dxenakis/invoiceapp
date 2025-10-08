package com.invoiceapp.companyscope;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 * Τα entities που είναι ανά εταιρεία κάνουν extends αυτό.
 * Τα GLOBAL (π.χ. VAT) ΔΕΝ το κληρονομούν.
 */
@MappedSuperclass
@FilterDef(name = "companyFilter", parameters = @ParamDef(name = "cid", type = Long.class))
@Filter(name = "companyFilter", condition = "company_id = :cid")
public abstract class CompanyScopedEntity {

    @Column(name = "company_id", nullable = false, updatable = false)
    protected Long companyId;

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
