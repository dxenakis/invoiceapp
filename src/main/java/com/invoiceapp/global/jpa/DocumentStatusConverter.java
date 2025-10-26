package com.invoiceapp.global.jpa;

import com.invoiceapp.global.DocumentStatus;
import com.invoiceapp.global.Effect;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class DocumentStatusConverter implements AttributeConverter<DocumentStatus, Integer> {
    @Override public Integer convertToDatabaseColumn(DocumentStatus attr) { return attr == null ? null : attr.getCode(); }
    @Override public DocumentStatus convertToEntityAttribute(Integer db) { return db == null ? null : DocumentStatus.fromCode(db); }
}