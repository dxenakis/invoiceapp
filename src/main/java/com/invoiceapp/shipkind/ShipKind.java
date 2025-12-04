package com.invoiceapp.shipkind;

import com.invoiceapp.shipkind.enums.MyDataShipKind;
import com.invoiceapp.shipkind.enums.MyDataShipKindConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "shipkind",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_shipkind_code",
                columnNames = { "code" }
        )
)
public class ShipKind {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Κωδικός σκοπού διακίνησης */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    /** Όνομα / περιγραφή */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /** Δεύτερη περιγραφή (προαιρετική) */
    @Column(name = "name1", length = 200)
    private String name1;

    /** Κωδικός myDATA για τον σκοπό διακίνησης */
    @Convert(converter = MyDataShipKindConverter.class)
    @Column(name = "mydata_shipkind_code")
    private MyDataShipKind mydataShipKind;

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

    // Getters - Setters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getName1() { return name1; }
    public MyDataShipKind getMydataShipKind() { return mydataShipKind; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code == null ? null : code.trim(); }
    public void setName(String name) { this.name = name == null ? null : name.trim(); }
    public void setName1(String name1) { this.name1 = name1 == null ? null : name1.trim(); }
    public void setMydataShipKind(MyDataShipKind mydataShipKind) { this.mydataShipKind = mydataShipKind; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
