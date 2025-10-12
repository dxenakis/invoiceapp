package com.invoiceapp.customer;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.country.Country;
import com.invoiceapp.country.CountryRepository;
import com.invoiceapp.customer.dto.CustomerRequest;
import com.invoiceapp.customer.dto.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequireTenant
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CountryRepository countryRepository;

    public CustomerServiceImpl(CustomerRepository repository, CountryRepository countryRepository) {
        this.repository = repository;
        this.countryRepository = countryRepository;
    }

    // --------- mapping helpers ---------
    private static CustomerResponse toDto(Customer c) {
        return new CustomerResponse(
                c.getId(),
                c.getCompanyId(), // γεμίζει αυτόματα από @TenantId
                c.getName(),
                c.getPhone(),
                c.getAddress(),
                c.getCity(),
                c.getZip(),
                (c.getCountry() != null ? c.getCountry().getId() : null),
                (c.getCountry() != null ? c.getCountry().getName() : null),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }

    private static void apply(CustomerRequest req, Customer e, Country country) {
        e.setName(req.name());
        e.setPhone(req.phone());
        e.setAddress(req.address());
        e.setCity(req.city());
        e.setZip(req.zip());
        e.setCountry(country);
        // e.setCompanyId(...) ΔΕΝ το αγγίζουμε — το βάζει το @TenantId/Resolver
    }

    private Customer getCustomerOr404(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private Country getCountryOr400(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid countryId"));
    }

    // --------- API ---------
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        return toDto(getCustomerOr404(id));
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Country country = getCountryOr400(request.countryId());
        Customer entity = new Customer();
        apply(request, entity, country);
        Customer saved = repository.save(entity);
        return toDto(saved);
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer existing = getCustomerOr404(id);
        Country country = getCountryOr400(request.countryId());
        apply(request, existing, country);
        Customer saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Customer existing = getCustomerOr404(id);
        repository.delete(existing);
    }
}
