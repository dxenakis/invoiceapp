// src/main/java/com/invoiceapp/global/jpa/MyDataPaymentMethodConverter.java
package com.invoiceapp.global.jpa;

import com.invoiceapp.global.MyDataPaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class MyDataPaymentMethodConverter implements AttributeConverter<MyDataPaymentMethod, Integer> {
    @Override public Integer convertToDatabaseColumn(MyDataPaymentMethod attr) { return attr == null ? null : attr.getCode(); }
    @Override public MyDataPaymentMethod convertToEntityAttribute(Integer db) { return db == null ? null : MyDataPaymentMethod.fromCode(db); }
}
