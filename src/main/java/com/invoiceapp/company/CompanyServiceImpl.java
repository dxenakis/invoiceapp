package com.invoiceapp.company;

import com.invoiceapp.access.UserCompanyAccessService;
import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;
import com.invoiceapp.securityconfig.SecurityUtils;
import com.invoiceapp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repo;
    private final UserRepository users; // ΝΕΟ
    private final UserCompanyAccessService access; // ΝΕΟ

    public CompanyServiceImpl(CompanyRepository repo,
                              UserRepository users,
                              UserCompanyAccessService access) {
        this.repo = repo;
        this.users = users;
        this.access = access;
    }

    @Transactional
    @Override
    public CompanyResponse createCompany(CompanyCreateRequest req) {
        Company company = new Company();
        company.setAfm(req.afm());
        company.setName(req.name());
        company.setAddressLine(req.addressLine());
        company.setCity(req.city());
        company.setPostalCode(req.postalCode());
        company.setCountryCode(req.countryCode());
        company.setEmail(req.email());
        company.setPhone(req.phone());
        return CompanyResponse.fromEntity(repo.save(company));
    }


    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long id) {
        // 404 αν δεν έχει πρόσβαση
        SecurityUtils.assertHasAccessOrNotFound(id, access, users);

        Company c = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        return CompanyResponse.fromEntity(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        // μόνο οι εταιρείες που έχει access ο χρήστης
        var companyIds = SecurityUtils.getAccessibleCompanyIds(access, users);
        return repo.findAllById(companyIds).stream()
                .map(CompanyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, String> getCompanyNamesByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Map.of();
        return repo.findAllById(ids).stream()
                .collect(Collectors.toMap(
                        Company::getId,
                        Company::getName   // ή το αντίστοιχο getter
                ));
    }


    @Override
    @Transactional
    public void deleteCompany(Long id) {
        // πρέπει να είναι COMPANY_ADMIN στη συγκεκριμένη εταιρεία
        SecurityUtils.requireCompanyAdmin(id, access, users);
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public CompanyResponse createOrGetByAfm(String vat,
                                            String name,
                                            String addressLine,
                                            String city,
                                            String postalCode,
                                            String countryCode,
                                            String email,
                                            String phone) {
        return repo.findByAfm(vat)
                .map(CompanyResponse::fromEntity) // αν υπάρχει ήδη, γύρνα την ίδια εταιρεία
                .orElseGet(() -> {
                    Company c = new Company();
                    c.setAfm(vat.trim());
                    c.setName(name.trim());
                    c.setAddressLine(addressLine);
                    c.setCity(city);
                    c.setPostalCode(postalCode);
                    c.setCountryCode(countryCode == null ? "GR" : countryCode);
                    c.setEmail(email);
                    c.setPhone(phone);
                    return CompanyResponse.fromEntity(repo.save(c));
                });
    }
}
