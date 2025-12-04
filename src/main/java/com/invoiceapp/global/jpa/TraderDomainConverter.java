package com.invoiceapp.global.jpa;

import com.invoiceapp.global.enums.TraderDomain;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = false)
public class TraderDomainConverter implements AttributeConverter<TraderDomain, Integer> {
    @Override public Integer convertToDatabaseColumn(TraderDomain attr) { return attr == null ? null : attr.getCode(); }
    @Override public TraderDomain convertToEntityAttribute(Integer db) { return db == null ? null : TraderDomain.fromCode(db); }
}


