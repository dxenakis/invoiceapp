package com.invoiceapp.mtrunit.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class MyDataMtrUnitCodeConverter implements AttributeConverter<MyDataMtrUnitCode, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MyDataMtrUnitCode attr) {
        return attr == null ? null : attr.getCode();
    }

    @Override
    public MyDataMtrUnitCode convertToEntityAttribute(Integer db) {
        return db == null ? null : MyDataMtrUnitCode.fromCode(db);
    }
}
