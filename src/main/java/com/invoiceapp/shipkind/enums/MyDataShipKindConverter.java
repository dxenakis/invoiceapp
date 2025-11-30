package com.invoiceapp.shipkind.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class MyDataShipKindConverter implements AttributeConverter<MyDataShipKind, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MyDataShipKind attr) {
        return attr == null ? null : attr.getCode();
    }

    @Override
    public MyDataShipKind convertToEntityAttribute(Integer db) {
        return db == null ? null : MyDataShipKind.fromCode(db);
    }
}
