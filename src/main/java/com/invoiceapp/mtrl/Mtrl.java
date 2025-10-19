package com.invoiceapp.mtrl;


import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

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

    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Column(name = "name1",length = 200)
    private String name1;
    @Column(name = "accountCategory", nullable = false, length = 200)
    private AccountingCategory accountCategory;
    @Column(name = "pricer")
    private float pricer;
    @Column(name = "pricew")
    private float pricew;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
