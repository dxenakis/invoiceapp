package com.invoiceapp.access;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCompanyAccessRepository extends JpaRepository<UserCompanyAccess, Long> {

    boolean existsByUserIdAndCompanyId(Long userId, Long companyId);

    Optional<UserCompanyAccess> findByUserIdAndCompanyId(Long userId, Long companyId);

    List<UserCompanyAccess> findAllByUserId(Long userId);

    long countByCompanyId(Long companyId);
}
