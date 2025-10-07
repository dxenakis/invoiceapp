package com.invoiceapp.access;

import com.invoiceapp.user.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserCompanyAccessServiceImpl implements UserCompanyAccessService {

    private final UserCompanyAccessRepository repo;

    public UserCompanyAccessServiceImpl(UserCompanyAccessRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public void grantAccess(Long userId, Long companyId, Role role) {
        // αν υπάρχει ήδη, δεν κάνουμε τίποτα (idempotent)
        if (!repo.existsByUserIdAndCompanyId(userId, companyId)) {
            UserCompanyAccess access = new UserCompanyAccess();
            access.setUserId(userId);
            access.setCompanyId(companyId);
            access.setRole(role);
            repo.save(access);
        } else {
            // προαιρετικά: ενημέρωσε ρόλο αν θες να αλλάζει
            repo.findByUserIdAndCompanyId(userId, companyId).ifPresent(a -> {
                if (a.getRole() != role) {
                    a.setRole(role);
                    repo.save(a);
                }
            });
        }
    }

    @Override
    @Transactional
    public void revokeAccess(Long userId, Long companyId) {
        repo.findByUserIdAndCompanyId(userId, companyId).ifPresent(repo::delete);
    }

    @Override
    public boolean hasAccess(Long userId, Long companyId) {
        return repo.existsByUserIdAndCompanyId(userId, companyId);
    }

    @Override
    public Optional<Role> roleFor(Long userId, Long companyId) {
        return repo.findByUserIdAndCompanyId(userId, companyId).map(UserCompanyAccess::getRole);
    }

    @Override
    public List<UserCompanyAccess> getUserCompanies(Long userId) {
        return repo.findAllByUserId(userId);
    }

    @Override
    public long countUsersForCompany(Long companyId) {
        return repo.countByCompanyId(companyId);
    }
}
