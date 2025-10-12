package com.invoiceapp.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Με Hibernate @TenantId ενεργό, όλα τα queries φιλτράρονται από το τρέχον tenant.
    // Αν θες “διπλή ασφάλεια”, μπορείς να προσθέσεις και: Optional<Customer> findByIdAndCompanyId(Long id, Long companyId);
}
