package com.invoiceapp.mtrl;


import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.Instant;
import java.time.LocalDateTime;
import com.invoiceapp.mtrunit.MtrUnit;
import com.invoiceapp.vat.Vat;
@Entity
@Table(
        name = "mtrl",
        indexes = {
                @Index(name = "idx_mtrl_company", columnList = "company_id")
        }
)
public class Mtrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Discriminator column για multi-tenancy (Hibernate 6). */
    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @Column(name = "code", nullable = false, length = 200)
    private String code;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Column(name = "name1",length = 200)
    private String name1;
    @Column(name = "accountCategory", nullable = false, length = 200)
    private AccountingCategory accountCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mtrunit_id")
    private MtrUnit mtrUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vat_id")
    private Vat vat;

    @Column(name = "pricer")
    private float pricer;
    @Column(name = "pricew")
    private float pricew;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public float getPricew() {
        return pricew;
    }

    public void setPricew(float pricew) {
        this.pricew = pricew;
    }

    public float getPricer() {
        return pricer;
    }

    public void setPricer(float pricer) {
        this.pricer = pricer;
    }

    public AccountingCategory getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(AccountingCategory accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }
    public String getCode(){ return  this.code;}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setCode(String code) { this.code = code;}
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public MtrUnit getMtrUnit() {
        return mtrUnit;
    }

    public void setMtrUnit(MtrUnit mtrUnit) {
        this.mtrUnit = mtrUnit;
    }

    public Vat getVat() {
        return vat;
    }

    public void setVat(Vat vat) {
        this.vat = vat;
    }

}
