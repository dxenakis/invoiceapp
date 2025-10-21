package com.invoiceapp.findoc;

import com.invoiceapp.mtrl.Mtrl;
import com.invoiceapp.vat.Vat;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.math.BigDecimal;

@Entity
@Table(name = "mtrlines",
        indexes = {
                @Index(name = "idx_mtrlines_findoc", columnList = "findoc"),
                @Index(name = "idx_mtrlines_company", columnList = "company_id")
        })
public class Mtrlines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "company_id", nullable = false, updatable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "findoc", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mtrlines_findoc"))
    private Findoc findoc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mtrl", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mtrlines_mtrl"))
    private Mtrl mtrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vat", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mtrlines_vat"))
    private Vat vat;

    @Column(name = "lineNo", nullable = false)
    private Integer lineNo;

    @Column(name = "qty", nullable = false, precision = 18, scale = 4)
    private BigDecimal qty = BigDecimal.ONE;

    @Column(name = "price", nullable = false, precision = 18, scale = 4)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "discount_rate", nullable = false, precision = 9, scale = 6)
    private BigDecimal discountRate = BigDecimal.ZERO; // 0..1

    @Column(name = "net_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "vat_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public Long getId() { return id; }
    public Long getCompanyId() { return companyId; }
    public Findoc getFindoc() { return findoc; }
    public Mtrl getMtrl() { return mtrl; }
    public Vat getVat() { return vat; }
    public Integer getLineNo() { return lineNo; }
    public BigDecimal getQty() { return qty; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public BigDecimal getNetAmount() { return netAmount; }
    public BigDecimal getVatAmount() { return vatAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }

    public void setId(Long id) { this.id = id; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setFindoc(Findoc findoc) { this.findoc = findoc; }
    public void setMtrl(Mtrl mtrl) { this.mtrl = mtrl; }
    public void setVat(Vat vat) { this.vat = vat; }
    public void setLineNo(Integer lineNo) { this.lineNo = lineNo; }
    public void setQty(BigDecimal qty) { this.qty = qty; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
    public void setVatAmount(BigDecimal vatAmount) { this.vatAmount = vatAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
