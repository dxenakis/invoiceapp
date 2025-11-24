// src/main/java/com/invoiceapp/payment/PaymentMethod.java
package com.invoiceapp.payment;

import com.invoiceapp.payment.enums.MyDataPaymentMethod;
import com.invoiceapp.payment.enums.MyDataPaymentMethodConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment",
        uniqueConstraints = @UniqueConstraint(name = "uk_payment_method_company_code", columnNames = {"company_id","code"}),
        indexes = @Index(name = "ix_payment_method_company_active", columnList = "company_id,active")
)
public class PaymentMethod {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Convert(converter = MyDataPaymentMethodConverter.class)
    @Column(name = "mydata_method", nullable = false)
    private MyDataPaymentMethod mydataMethod; // DB: smallint code

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @CreationTimestamp @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // getters/setters...
    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public MyDataPaymentMethod getMydataMethod() { return mydataMethod; }
    public void setMydataMethod(MyDataPaymentMethod mydataMethod) { this.mydataMethod = mydataMethod; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
