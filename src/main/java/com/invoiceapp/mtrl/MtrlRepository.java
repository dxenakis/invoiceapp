package com.invoiceapp.mtrl;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface MtrlRepository extends JpaRepository<Mtrl, Long> {
    @Override
    Optional<Mtrl> findById(Long id);


    @Override
    List<Mtrl> findAll();


    @Override
    <S extends Mtrl> S save(S entity);
}
