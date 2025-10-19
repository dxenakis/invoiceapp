package com.invoiceapp.series;

import com.invoiceapp.branch.Branch;
import com.invoiceapp.documenttype.DocumentType;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "series",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_series_scope_code",
        columnNames = {"company_id", "branch_id", "document_type_id", "code"}
    ),
    indexes = {
        @Index(name = "idx_series_company", columnList = "company_id"),
        @Index(name = "idx_series_scope", columnList = "branch_id, document_type_id")
    }
)
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Multitenancy discriminator */
    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    /** Προαιρετικά: Σειρά συγκεκριμένου υποκαταστήματος */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", foreignKey = @ForeignKey(name = "fk_series_branch"))
    private Branch branch;

    /** Υποχρεωτικά: αφορά συγκεκριμένο τύπο παραστατικού */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_series_document_type"))
    private DocumentType documentType;

    /** Κωδικός σειράς (π.χ. "A", "B", "2025A") */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    /** Προαιρετικό σταθερό prefix (π.χ. "A-" ή "2025/") */
    @Column(name = "prefix", length = 30)
    private String prefix;

    /** Προαιρετικό πρότυπο μορφοποίησης, π.χ. "{YYYY}/{SERIES}-{NNNN}" */
    @Column(name = "format_pattern", length = 80)
    private String formatPattern;

    /** Μήκος padding του αριθμού (π.χ. 4 => 0001) */
    @Column(name = "padding_length")
    private Integer paddingLength = 4;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters/Setters
    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public Branch getBranch() { return branch; }
    public DocumentType getDocumentType() { return documentType; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public String getPrefix() { return prefix; }
    public String getFormatPattern() { return formatPattern; }
    public Integer getPaddingLength() { return paddingLength; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setBranch(Branch branch) { this.branch = branch; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public void setCode(String code) { this.code = code == null ? null : code.trim(); }
    public void setDescription(String description) { this.description = description == null ? null : description.trim(); }
    public void setActive(boolean active) { this.active = active; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public void setFormatPattern(String formatPattern) { this.formatPattern = formatPattern; }
    public void setPaddingLength(Integer paddingLength) { this.paddingLength = paddingLength; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
