package com.invoiceapp.country;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "countries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_iso_code", columnNames = "iso_code")

        }
)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Δίγραμμος κωδικός ISO 3166-1 alpha-2 (π.χ. GR, US). */
    @Column(name = "iso_code", nullable = false, length = 2)
    private String isoCode;


    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Country() {}

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        normalize();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        normalize();
    }

    private void normalize() {
        if (this.isoCode != null) this.isoCode = this.isoCode.trim().toUpperCase();
        if (this.name != null) this.name = this.name.trim();
    }

    // Getters / Setters
    public Long getId() { return id; }
    public String getIsoCode() { return isoCode; }
    public void setIsoCode(String iso2) { this.isoCode = iso2; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
