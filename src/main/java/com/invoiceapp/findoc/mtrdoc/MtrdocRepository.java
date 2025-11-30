package com.invoiceapp.findoc.mtrdoc;

import com.invoiceapp.findoc.Findoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MtrdocRepository extends JpaRepository<Mtrdoc, Long> {

    Optional<Mtrdoc> findByFindoc(Findoc findoc);

    Optional<Mtrdoc> findByFindocId(Long findocId);
}
