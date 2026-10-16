package com.nexusengine.core.domain.shared.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;

@Converter
public class IntegerToDeletedAtConverter implements AttributeConverter<Integer, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(Integer attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute == 1 ? LocalDateTime.now() : null;
    }

    @Override
    public Integer convertToEntityAttribute(LocalDateTime dbData) {
        return dbData != null ? 1 : 0;
    }
}
