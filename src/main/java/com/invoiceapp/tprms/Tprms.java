package com.invoiceapp.tprms;


import com.invoiceapp.global.Effect;
import com.invoiceapp.global.Sign;
import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.findoc.enums.DocumentDomainConverter;
import com.invoiceapp.global.jpa.EffectConverter;
import com.invoiceapp.global.jpa.SignConverter;
import jakarta.persistence.*;
import java.time.Instant;


/*
 * tprms αναπαριστά την κίνηση που θα προκαλεί το παραστατικό που το φέρει, στο κύκλωμα των συναλλασόμενων.
 * Στην ουσία επηρεάζει τα πεδία, χρέωσης, πίστωσης και τζίρου.
 *
 * */

@Entity
@Table(
        name = "tprms",
        uniqueConstraints = @UniqueConstraint(name = "uk_tprms_code", columnNames = "code")
)
public class Tprms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Μοναδικός κωδικός (π.χ. INV_SALE, CRN_SALE) */
    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String description;

    /** Αγορές/Πωλήσεις/Εισπράξεις/Πληρωμές */
    @Convert(converter = DocumentDomainConverter.class)
    @Column(nullable = false, length = 20)
    private DocumentDomain domain;

    /** Επίδραση στη Χρέωση */
    @Convert(converter = EffectConverter.class)
    @Column(nullable = false, length = 10)
    private Effect debit;

    /** Επίδραση στην Πίστωση */
    @Convert(converter = EffectConverter.class)
    @Column(nullable = false, length = 10)
    private Effect credit;

    /** Επίδραση στον Τζίρο */
    @Convert(converter = EffectConverter.class)
    @Column(nullable = false, length = 10)
    private Effect turnover;

    /** Πρόσημο εμφάνισης */
    @Convert(converter = SignConverter.class)
    @Column(nullable = false, length = 10)
    private Sign sign;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public DocumentDomain getDomain() { return domain; }
    public Effect getDebit() { return debit; }
    public Effect getCredit() { return credit; }
    public Effect getTurnover() { return turnover; }
    public Sign getSign() { return sign; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setDomain(DocumentDomain domain) { this.domain = domain; }
    public void setDebit(Effect debit) { this.debit = debit; }
    public void setCredit(Effect credit) { this.credit = credit; }
    public void setTurnover(Effect turnover) { this.turnover = turnover; }
    public void setSign(Sign sign) { this.sign = sign; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
