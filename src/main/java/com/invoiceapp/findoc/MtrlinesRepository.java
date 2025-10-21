package com.invoiceapp.findoc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MtrlinesRepository extends JpaRepository<Mtrlines, Long> {
    List<Mtrlines> findByFindocIdOrderByLineNoAsc(Long findocId);
}
