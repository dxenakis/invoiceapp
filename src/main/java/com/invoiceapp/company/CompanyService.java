package com.invoiceapp.company;

import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CompanyService {
    CompanyResponse createCompany(CompanyCreateRequest req);
    CompanyResponse getCompanyById(Long id);
    Map<Long, String> getCompanyNamesByIds(Collection<Long> ids);
    List<CompanyResponse> getAllCompanies();
    CompanyResponse updateCompany(Long id, CompanyCreateRequest req);
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
