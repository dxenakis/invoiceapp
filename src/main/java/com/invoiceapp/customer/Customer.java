package com.invoiceapp.customer;

import com.invoiceapp.company.Company;
import com.invoiceapp.country.Country;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_customers_company", columnList = "company_id")
        }
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Discriminator column για multi-tenancy (Hibernate 6). */
    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /** Foreign key προς Company. Read-only helper σχέση. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_customers_company"))
    private Company company;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 500)
    private String city;

    @Column(name = "zip", length = 500)
    private String zip;

    /** Global αναφορά σε Country (χωρίς index, όπως ζήτησες). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_customers_country"))
    private Country country;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public Customer() {}

    // --- Lifecycle hooks ---
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        normalize();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        normalize();
    }

    private void normalize() {
        if (this.name != null) this.name = this.name.trim();
        if (this.phone != null) this.phone = this.phone.trim();
        if (this.address != null) this.address = this.address.trim();
        if (this.city != null) this.city = this.city.trim();
        if (this.zip != null) this.zip = this.zip.trim();
    }

    // --- Getters / Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    /** Συνήθως δεν το θέτεις χειροκίνητα: το @TenantId θα το συμπληρώσει στα INSERTs. */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null/blank");
        }
        this.name = name.trim();
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = (phone == null ? null : phone.trim()); }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = (address == null ? null : address.trim()); }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = (city == null ? null : city.trim()); }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = (zip == null ? null : zip.trim()); }

    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
