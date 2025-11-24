package com.invoiceapp.vat.enums;

import com.invoiceapp.vat.enums.MyDataVatCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class MyDataVatCodeConverter implements AttributeConverter<MyDataVatCode, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MyDataVatCode attr) {
        return attr == null ? null : attr.getCode();
    }

    @Override
    public MyDataVatCode convertToEntityAttribute(Integer db) {
        return db == null ? null : MyDataVatCode.fromCode(db);
    }
}