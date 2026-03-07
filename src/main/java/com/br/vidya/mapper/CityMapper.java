package com.br.vidya.mapper;

import com.br.vidya.dto.response.CityResponse;
import com.br.vidya.integration.sankhya.dto.LoadRecordsResponse;
import com.br.vidya.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityResponse toResponse(City city);

    @Mapping(target = "codCid", source = "f0", qualifiedByName = "smFieldToLong")
    @Mapping(target = "name", source = "f1.value")
    @Mapping(target = "uf", source = "f3.value")
    City toCity(LoadRecordsResponse.Entity entity);

    default List<City> toCityList(List<LoadRecordsResponse.Entity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toCity).toList();
    }

    @Named("smFieldToLong")
    default Long smFieldToLong(LoadRecordsResponse.SmField field) {
        if (field == null || field.value() == null || field.value().isBlank()) return null;
        try {
            return Long.parseLong(field.value().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
