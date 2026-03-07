package com.br.vidya.model.converter;

import com.br.vidya.model.enums.PersonType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PersonTypeConverter implements AttributeConverter<PersonType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PersonType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public PersonType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : PersonType.fromCode(dbData);
    }
}
