package com.invoiceapp.mtrunit;

import com.invoiceapp.mtrunit.enums.MyDataMtrUnitCode;
import com.invoiceapp.mtrunit.enums.MyDataMtrUnitCodeConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "mtrunit",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_mtrunit_code",
                columnNames = { "code" }
        )
)
public class MtrUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Κωδικός μονάδας (π.χ. TM, KG) */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    /** Όνομα (π.χ. Τεμάχια) */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** Δεύτερο όνομα / εναλλακτική περιγραφή */
    @Column(name = "name1", length = 100)
    private String name1;

    /** ISO code (π.χ. C62, KGM κλπ. αν τα βάλεις) */
    @Column(name = "iso_code", length = 10)
    private String isoCode;

    /** Κωδικός myDATA για τη μονάδα μέτρησης */
    @Convert(converter = MyDataMtrUnitCodeConverter.class)
    @Column(name = "mydata_mtrunit_code")
    private MyDataMtrUnitCode mydataCode;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters / Setters

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getName1() { return name1; }
    public String getIsoCode() { return isoCode; }
    public MyDataMtrUnitCode getMydataCode() { return mydataCode; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code == null ? null : code.trim(); }
    public void setName(String name) { this.name = name == null ? null : name.trim(); }
    public void setName1(String name1) { this.name1 = name1 == null ? null : name1.trim(); }
    public void setIsoCode(String isoCode) { this.isoCode = isoCode == null ? null : isoCode.trim(); }
    public void setMydataCode(MyDataMtrUnitCode mydataCode) { this.mydataCode = mydataCode; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
