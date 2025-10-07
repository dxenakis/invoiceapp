package com.invoiceapp.access;

import com.invoiceapp.user.Role;

import java.util.List;
import java.util.Optional;

public interface UserCompanyAccessService {

    /** Δίνει πρόσβαση (αν δεν υπάρχει ήδη) σε user για συγκεκριμένη εταιρεία με ρόλο. */
    void grantAccess(Long userId, Long companyId, Role role);

    /** Αφαιρεί πρόσβαση (αν υπάρχει). */
    void revokeAccess(Long userId, Long companyId);

    /** Ελέγχει αν ο χρήστης έχει πρόσβαση στην εταιρεία. */
    boolean hasAccess(Long userId, Long companyId);

    /** Επιστρέφει τον ρόλο του χρήστη για την εταιρεία, αν υπάρχει. */
    Optional<Role> roleFor(Long userId, Long companyId);

    /** Λίστα όλων των εγγραφών πρόσβασης του χρήστη (για UI “switch company”). */
    List<UserCompanyAccess> getUserCompanies(Long userId);

    /** Πλήθος χρηστών που έχουν πρόσβαση σε μια εταιρεία. Χρήσιμο για “πρώτος χρήστης”. */
    long countUsersForCompany(Long companyId);
}
