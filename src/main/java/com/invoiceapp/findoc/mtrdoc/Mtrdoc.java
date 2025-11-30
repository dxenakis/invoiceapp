package com.invoiceapp.findoc.mtrdoc;

import com.invoiceapp.findoc.Findoc;
import com.invoiceapp.whouse.Whouse;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "mtrdoc",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_mtrdoc_company_findoc",
                columnNames = {"company_id", "findoc_id"}
        ),
        indexes = {
                @Index(name = "idx_mtrdoc_company", columnList = "company_id"),
                @Index(name = "idx_mtrdoc_findoc", columnList = "findoc_id")
        }
)
public class Mtrdoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /** 1–1 με Findoc (κάθε παραστατικό έχει 0 ή 1 mtrdoc) */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "findoc_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mtrdoc_findoc")
    )
    private Findoc findoc;

    /** Στοιχεία παράδοσης */
    @Column(name = "address_line1", length = 120)
    private String addressLine1;

    @Column(name = "address_line2", length = 120)
    private String addressLine2;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "region", length = 80)
    private String region;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    /** ISO-2 κωδικός χώρας, π.χ. GR, DE κτλ. */
    @Column(name = "country_code", length = 2)
    private String countryCode;

    /** Από ποια/σε ποια αποθήκη σχετίζεται η παράδοση για το παραστατικό */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "whouse_id",
            foreignKey = @ForeignKey(name = "fk_mtrdoc_whouse")
    )
    private Whouse whouse;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ========= Getters / Setters =========

    public Long getId() { return id; }

    public Long getCompanyId() { return companyId; }
    /** Συνήθως το @TenantId το γεμίζει μόνο του, δεν το βάζεις χειροκίνητα. */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Findoc getFindoc() { return findoc; }
    public void setFindoc(Findoc findoc) { this.findoc = findoc; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public Whouse getWhouse() { return whouse; }
    public void setWhouse(Whouse whouse) { this.whouse = whouse; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
