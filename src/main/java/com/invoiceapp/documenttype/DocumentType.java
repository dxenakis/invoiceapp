package com.invoiceapp.documenttype;

import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.company.Company;
import com.invoiceapp.findoc.enums.DocumentDomainConverter;
import com.invoiceapp.iteprms.ItePrms;
import com.invoiceapp.tprms.Tprms;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "document_types",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_document_types_company_code",
                columnNames = {"company_id", "code"}
        ),
        indexes = @Index(name = "idx_document_types_company", columnList = "company_id")
)
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Discriminator για multitenancy – γεμίζει από Hibernate */
    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /** Read-only helper σχέση προς Company (προαιρετικό) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Company company;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    /** Αγορές/Πωλήσεις/Εισπράξεις/Πληρωμές */
    @Convert(converter = DocumentDomainConverter.class)
    @Column(name = "domain", nullable = false, length = 20)
    private DocumentDomain domain;

    /** Κίνηση πελάτη (TPRMS) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tprms_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_types_tprms"))
    private Tprms tprms;

    /** Κίνηση αποθήκης (ItePrms) – προαιρετική εάν ο τύπος αφορά μόνο υπηρεσίες */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iteprms_id",
            foreignKey = @ForeignKey(name = "fk_document_types_iteprms"))
    private ItePrms itePrms;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // --- getters/setters ---
    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public Company getCompany() { return company; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public DocumentDomain getDomain() { return domain; }
    public Tprms getTprms() { return tprms; }
    public ItePrms getItePrms() { return itePrms; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setCompany(Company company) { this.company = company; }
    public void setCode(String code) { this.code = code == null ? null : code.trim(); }
    public void setDescription(String description) { this.description = description == null ? null : description.trim(); }
    public void setDomain(DocumentDomain domain) { this.domain = domain; }
    public void setTprms(Tprms tprms) { this.tprms = tprms; }
    public void setItePrms(ItePrms itePrms) { this.itePrms = itePrms; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
