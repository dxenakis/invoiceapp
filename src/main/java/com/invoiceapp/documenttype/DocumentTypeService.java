package com.invoiceapp.documenttype;

import com.invoiceapp.documenttype.dto.DocumentTypeRequest;
import com.invoiceapp.documenttype.dto.DocumentTypeResponse;
import com.invoiceapp.findoc.enums.DocumentDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentTypeService {

    DocumentTypeResponse getById(Long id);

    DocumentTypeResponse getByCode(String code);

    Page<DocumentTypeResponse> list(Pageable pageable);

    Page<DocumentTypeResponse> listByDomain(DocumentDomain domain, Pageable pageable);

    DocumentTypeResponse create(DocumentTypeRequest request);

    DocumentTypeResponse update(Long id, DocumentTypeRequest request);

    void delete(Long id);
}
