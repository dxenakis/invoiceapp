package com.invoiceapp.access;

import com.invoiceapp.user.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

@Entity
@Table(name = "user_company_access") // snake_case για SQL
public class UserCompanyAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)  // snake_case στη βάση
    private Long userId;


    @Column(name = "company_id", nullable = false) // snake_case στη βάση
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role; // VIEWER, ACCOUNTANT, COMPANY_ADMIN

    // --- getters/setters ---
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getCompanyId() { return companyId; }
    public Role getRole() { return role; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setRole(Role role) { this.role = role; }
}
