package com.invoiceapp.company;

import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;
import java.util.List;

public interface CompanyService {
    CompanyResponse createCompany(CompanyCreateRequest req);
    CompanyResponse getCompanyById(Long id);
    List<CompanyResponse> getAllCompanies();
    void deleteCompany(Long id);
    CompanyResponse createOrGetByAfm(String vatNumber,
                                     String name,
                                     String addressLine,
                                     String city,
                                     String postalCode,
                                     String countryCode,
                                     String email,
                                     String phone);
}
