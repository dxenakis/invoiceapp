package com.invoiceapp.mtrl;

import java.util.List;


public interface MtrlService {
    List<Mtrl> listAll();
    Mtrl getById(Long id);
    Mtrl create(Mtrl mtrl);
    Mtrl update(Long id, Mtrl mtrl);
    void delete(Long id);
}