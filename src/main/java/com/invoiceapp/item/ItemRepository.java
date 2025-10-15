package com.invoiceapp.item;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {
    @Override
    Optional<Item> findById(Long id);


    @Override
    List<Item> findAll();


    @Override
    <S extends Item> S save(S entity);
}
