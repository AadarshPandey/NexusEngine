package com.nexusengine.core.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntegerToBooleanConverter implements AttributeConverter<Integer, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(Integer attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute == 1;
    }

    @Override
    public Integer convertToEntityAttribute(Boolean dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData ? 1 : 0;
    }
}
