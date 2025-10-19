package com.invoiceapp.iteprms;


import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;
import jakarta.persistence.*;


@Entity
@Table(

        name = "iteprms",
        uniqueConstraints = @UniqueConstraint(name = "uk_iteprms_code", columnNames = "code")
)
public class ItePrms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Μοναδικός κωδικός (π.χ. INV_SALE, CRN_SALE) */
    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String description;

    /** Αγορές/Πωλήσεις/Εισπράξεις/Πληρωμές */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentDomain domain;

    /** Επίδραση στη Ποσότητα Εισαγώγών */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect impqty;

    /** Επίδραση στη αξία Εισαγώγών */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect impval;

    /** Επίδραση στη Ποσότητα Εξαγωγών */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect expqty;

    /** Επίδραση στη αξία Εξαγωγών */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect expval;


    /** Επίδραση στη Ποσότητα Αγορών */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect purqty;

    /** Επίδραση στη αξία Αγορών */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect purval;

    /** Επίδραση στη Ποσότητα Πωλήσεων */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect salqty;

    /** Επίδραση στη αξία Πωλήσεων */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Effect salval;


    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentDomain getDomain() {
        return domain;
    }

    public void setDomain(DocumentDomain domain) {
        this.domain = domain;
    }

    public Effect getImpqty() {
        return impqty;
    }

    public void setImpqty(Effect impqty) {
        this.impqty = impqty;
    }

    public Effect getImpval() {
        return impval;
    }

    public void setImpval(Effect impval) {
        this.impval = impval;
    }

    public Effect getExpqty() {
        return expqty;
    }

    public void setExpqty(Effect expqty) {
        this.expqty = expqty;
    }

    public Effect getExpval() {
        return expval;
    }

    public void setExpval(Effect expval) {
        this.expval = expval;
    }

    public Effect getPurqty() {
        return purqty;
    }

    public void setPurqty(Effect purqty) {
        this.purqty = purqty;
    }

    public Effect getPurval() {
        return purval;
    }

    public void setPurval(Effect purval) {
        this.purval = purval;
    }

    public Effect getSalqty() {
        return salqty;
    }

    public void setSalqty(Effect salqty) {
        this.salqty = salqty;
    }

    public Effect getSalval() {
        return salval;
    }

    public void setSalval(Effect salval) {
        this.salval = salval;
    }
}




