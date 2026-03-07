package com.br.vidya.model.converter;

import com.br.vidya.model.enums.IcmsType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IcmsTypeConverter implements AttributeConverter<IcmsType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(IcmsType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public IcmsType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : IcmsType.fromCode(dbData);
    }
}
