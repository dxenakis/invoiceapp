package com.invoiceapp.country;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByIsoCode(String isoCode);
    Optional<Country> findById(Long id);
    List<Country> findAll();
    Country save(Country c);


}
