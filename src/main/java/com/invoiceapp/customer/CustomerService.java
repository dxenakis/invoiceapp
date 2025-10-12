package com.invoiceapp.customer;

import com.invoiceapp.customer.dto.CustomerRequest;
import com.invoiceapp.customer.dto.CustomerResponse;

public interface CustomerService {
    CustomerResponse getById(Long id);
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Long id, CustomerRequest request);
    void delete(Long id);
}
