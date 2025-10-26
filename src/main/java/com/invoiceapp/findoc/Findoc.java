package com.invoiceapp.findoc;

import com.invoiceapp.branch.Branch;
import com.invoiceapp.customer.Customer;
import com.invoiceapp.documenttype.DocumentType;
import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.DocumentStatus;
import com.invoiceapp.global.jpa.DocumentDomainConverter;
import com.invoiceapp.series.Series;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "findoc",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_findoc_number_scope",
                columnNames = {
                        "company_id","document_type_id","branch_id","series_id","fiscal_year","number"
                }
        ),
        indexes = {
                @Index(name = "idx_findoc_company", columnList = "company_id"),
                @Index(name = "idx_findoc_dates", columnList = "document_date,fiscal_year"),
                @Index(name = "idx_findoc_customer", columnList = "customer_id")
        }
)
public class Findoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_type_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_findoc_document_type"))
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id",
            foreignKey = @ForeignKey(name = "fk_findoc_branch"))
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id",
            foreignKey = @ForeignKey(name = "fk_findoc_series"))
    private Series series;

    @Convert(converter = DocumentDomainConverter.class)
    @Column(name = "domain", nullable = false, length = 20)
    private DocumentDomain documentDomain;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "number")
    private Long number;

    @Column(name = "printed_number", length = 80)
    private String printedNumber;

    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_findoc_customer"))
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private DocumentStatus status = DocumentStatus.DRAFT;

    @Column(name = "total_net", nullable = false)
    private BigDecimal totalNet = BigDecimal.ZERO;

    @Column(name = "total_vat", nullable = false)
    private BigDecimal totalVat = BigDecimal.ZERO;

    @Column(name = "total_gross", nullable = false)
    private BigDecimal totalGross = BigDecimal.ZERO;

    @OneToMany(mappedBy = "findoc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mtrlines> lines = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public void addLine(Mtrlines line) {
        line.setFindoc(this);
        this.lines.add(line);
    }

    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public DocumentType getDocumentType() { return documentType; }
    public Branch getBranch() { return branch; }
    public Series getSeries() { return series; }
    public DocumentDomain getDocumentDomain() { return documentDomain; }
    public Integer getFiscalYear() { return fiscalYear; }
    public Long getNumber() { return number; }
    public String getPrintedNumber() { return printedNumber; }
    public LocalDate getDocumentDate() { return documentDate; }
    public Customer getCustomer() { return customer; }
    public DocumentStatus getStatus() { return status; }
    public BigDecimal getTotalNet() { return totalNet; }
    public BigDecimal getTotalVat() { return totalVat; }
    public BigDecimal getTotalGross() { return totalGross; }
    public List<Mtrlines> getLines() { return lines; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public void setBranch(Branch branch) { this.branch = branch; }
    public void setSeries(Series series) { this.series = series; }
    public void setDocumentDomain(DocumentDomain documentDomain) { this.documentDomain = documentDomain; }
    public void setFiscalYear(Integer fiscalYear) { this.fiscalYear = fiscalYear; }
    public void setNumber(Long number) { this.number = number; }
    public void setPrintedNumber(String printedNumber) { this.printedNumber = printedNumber; }
    public void setDocumentDate(LocalDate documentDate) { this.documentDate = documentDate; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setStatus(DocumentStatus status) { this.status = status; }
    public void setTotalNet(BigDecimal totalNet) { this.totalNet = totalNet; }
    public void setTotalVat(BigDecimal totalVat) { this.totalVat = totalVat; }
    public void setTotalGross(BigDecimal totalGross) { this.totalGross = totalGross; }
    public void setLines(List<Mtrlines> lines) { this.lines = lines; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
