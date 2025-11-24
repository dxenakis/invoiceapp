package com.invoiceapp.global.jpa;


import com.invoiceapp.global.Sign;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class SignConverter implements AttributeConverter<Sign, Integer> {
    @Override public Integer convertToDatabaseColumn(Sign attr) { return attr == null ? null : attr.getCode(); }
    @Override public Sign convertToEntityAttribute(Integer db) { return db == null ? null : Sign.fromCode(db); }
}

