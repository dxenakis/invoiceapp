package com.invoiceapp.branch;

import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "branches",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_branches_company_code",
                columnNames = {"company_id", "code"}
        ),
        indexes = @Index(name = "idx_branches_company", columnList = "company_id")
)
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Multitenancy discriminator (basic field) */
    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /** Μοναδικός κωδικός μέσα στην εταιρεία (π.χ. ATH, THES, HQ) */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    // Διεύθυνση / Επικοινωνία (προαιρετικά)
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

    /** ISO 3166-1 alpha-2 (π.χ. GR) */
    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "phone", length = 40)
    private String phone;

    @Column(name = "email", length = 120)
    private String email;

    /** Προαιρετικό flag για κεντρικό */
    @Column(name = "headquarters", nullable = false)
    private boolean headquarters = false;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters / Setters
    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAddressLine1() { return addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public String getCity() { return city; }
    public String getRegion() { return region; }
    public String getPostalCode() { return postalCode; }
    public String getCountryCode() { return countryCode; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public boolean isHeadquarters() { return headquarters; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setCode(String code) { this.code = code == null ? null : code.trim(); }
    public void setName(String name) { this.name = name == null ? null : name.trim(); }
    public void setDescription(String description) { this.description = description == null ? null : description.trim(); }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public void setCity(String city) { this.city = city; }
    public void setRegion(String region) { this.region = region; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setHeadquarters(boolean headquarters) { this.headquarters = headquarters; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
