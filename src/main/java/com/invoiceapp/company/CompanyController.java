package com.invoiceapp.company;

import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    /**
     * Δημιουργία εταιρείας.
     * Αν το μοντέλο σου επιτρέπει μόνο σε συστημικούς admins να δημιουργούν εταιρείες,
     * άφησε το @PreAuthorize όπως είναι. Αν επιτρέπεται σε όλους, αφαίρεσέ το.
     */
    //@PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY_ADMIN')")
    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyCreateRequest req) {
        return ResponseEntity.ok(service.createCompany(req));
    }

    /**
     * Επιστρέφει μία εταιρεία ΜΟΝΟ αν ο τρέχων χρήστης έχει πρόσβαση σε αυτήν.
     * Το access check γίνεται μέσα στο service (μέσω SecurityUtils + UserCompanyAccessService).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCompanyById(id));
    }

    /**
     * Λίστα εταιρειών του τρέχοντος χρήστη (ΟΧΙ όλων).
     * Το φιλτράρισμα γίνεται μέσα στο service (βλέπει μόνο τις εταιρείες όπου ο χρήστης έχει access).
     */
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAll() {
        return ResponseEntity.ok(service.getAllCompanies());
    }

    /**
     * (Προαιρετικό) Αν χρησιμοποιείς "active company" στο JWT, βολεύει ένα helper endpoint.
     */
    @GetMapping("/active")
    public ResponseEntity<CompanyResponse> getActive() {
        return ResponseEntity.ok(service.getCompanyById(
                com.invoiceapp.securityconfig.SecurityUtils.getActiveCompanyIdOrThrow()
        ));
    }

    /**
     * Διαγραφή εταιρείας: απαιτεί COMPANY_ADMIN πάνω στη ΣΥΓΚΕΚΡΙΜΕΝΗ εταιρεία.
     * Το service κάνει και δεύτερο έλεγχο (defense-in-depth).
     */
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
