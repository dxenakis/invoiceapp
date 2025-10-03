package com.invoiceapp.company;

import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repo;

    public CompanyServiceImpl(CompanyRepository repo) {
        this.repo = repo;
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
    public CompanyResponse getCompanyById(Long id) {
        return repo.findById(id).map(CompanyResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        return repo.findAll().stream().map(CompanyResponse::fromEntity).toList();
    }

    @Override
    public void deleteCompany(Long id) {
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
