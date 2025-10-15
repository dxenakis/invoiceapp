package com.invoiceapp.item;

import java.util.List;


public interface ItemService {
    List<Item> listAll();
    Item getById(Long id);
    Item create(Item item);
    Item update(Long id, Item item);
    void delete(Long id);
}