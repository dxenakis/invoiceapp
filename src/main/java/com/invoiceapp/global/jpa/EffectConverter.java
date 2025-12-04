package com.invoiceapp.global.jpa;

import com.invoiceapp.global.enums.Effect;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EffectConverter implements AttributeConverter<Effect, Integer> {
    @Override public Integer convertToDatabaseColumn(Effect attr) { return attr == null ? null : attr.getCode(); }
    @Override public Effect convertToEntityAttribute(Integer db) { return db == null ? null : Effect.fromCode(db); }
}

