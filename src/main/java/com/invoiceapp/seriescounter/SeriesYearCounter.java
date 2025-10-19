package com.invoiceapp.seriescounter;

import com.invoiceapp.series.Series;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "series_year_counters",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_series_year_counter_scope",
        columnNames = {"company_id", "series_id", "fiscal_year"}
    ),
    indexes = {
        @Index(name = "idx_syc_company", columnList = "company_id"),
        @Index(name = "idx_syc_series_year", columnList = "series_id, fiscal_year")
    }
)
public class SeriesYearCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "series_id", nullable = false, foreignKey = @ForeignKey(name = "fk_syc_series"))
    private Series series;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "last_number", nullable = false)
    private Long lastNumber = 0L;

    @Column(name = "last_issued_at")
    private LocalDateTime lastIssuedAt;

    /** Optimistic locking (εναλλακτικά μπορείς να πας με PESSIMISTIC WRITE) */
    @Version
    private Long version;

    // Getters/Setters
    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public Series getSeries() { return series; }
    public Integer getFiscalYear() { return fiscalYear; }
    public Long getLastNumber() { return lastNumber; }
    public LocalDateTime getLastIssuedAt() { return lastIssuedAt; }
    public Long getVersion() { return version; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setSeries(Series series) { this.series = series; }
    public void setFiscalYear(Integer fiscalYear) { this.fiscalYear = fiscalYear; }
    public void setLastNumber(Long lastNumber) { this.lastNumber = lastNumber; }
    public void setLastIssuedAt(LocalDateTime lastIssuedAt) { this.lastIssuedAt = lastIssuedAt; }
    public void setVersion(Long version) { this.version = version; }
}
