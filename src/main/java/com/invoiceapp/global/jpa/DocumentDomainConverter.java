package com.invoiceapp.global.jpa;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Sign;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;



@Converter(autoApply = false)
public class DocumentDomainConverter implements AttributeConverter<DocumentDomain, Integer> {
    @Override public Integer convertToDatabaseColumn(DocumentDomain attr) { return attr == null ? null : attr.getCode(); }
    @Override public DocumentDomain convertToEntityAttribute(Integer db) { return db == null ? null : DocumentDomain.fromCode(db); }
}