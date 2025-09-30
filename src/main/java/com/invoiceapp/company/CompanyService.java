package com.invoiceapp.company;

import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;
import java.util.List;

public interface CompanyService {
    CompanyResponse createCompany(CompanyCreateRequest req);
    CompanyResponse getCompanyById(Long id);
    List<CompanyResponse> getAllCompanies();
    void deleteCompany(Long id);
}
