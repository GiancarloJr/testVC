package com.br.vidya.mapper;

import com.br.vidya.dto.request.ClientRequest;
import com.br.vidya.integration.sankhya.dto.LoadRecordsResponse;
import com.br.vidya.integration.sankhya.dto.SaveRecordRequest;
import com.br.vidya.dto.response.ClientResponse;
import com.br.vidya.model.City;
import com.br.vidya.model.Client;
import com.br.vidya.model.enums.IcmsType;
import com.br.vidya.model.enums.PersonType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "codParc", source = "codParc")
    @Mapping(target = "codCid", source = "city.codCid")
    @Mapping(target = "cityName", source = "city.name")
    ClientResponse toResponse(Client client);

    @Mapping(target = "codParc", ignore = true)
    @Mapping(target = "city", ignore = true)
    Client toEntity(ClientRequest request);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "codParc", source = "f0", qualifiedByName = "smFieldToLong")
    @Mapping(target = "nomeParc", source = "f1.value")
    @Mapping(target = "cgcCpf", source = "f2.value")
    @Mapping(target = "razaoSocial", source = "f3.value")
    @Mapping(target = "personType", source = "f4", qualifiedByName = "smFieldToPersonType")
    @Mapping(target = "classificms", source = "f5", qualifiedByName = "smFieldToIcmsType")
    Client toClient(LoadRecordsResponse.Entity entity);

    default Client toClient(LoadRecordsResponse.Entity entity, City city) {
        Client client = toClient(entity);
        client.setCity(city);
        return client;
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

    @Named("smFieldToPersonType")
    default PersonType smFieldToPersonType(LoadRecordsResponse.SmField field) {
        if (field == null || field.value() == null || field.value().isBlank()) return null;
        return PersonType.fromSankhyaCode(field.value().trim());
    }

    @Named("smFieldToIcmsType")
    default IcmsType smFieldToIcmsType(LoadRecordsResponse.SmField field) {
        if (field == null || field.value() == null || field.value().isBlank()) return null;
        try {
            return IcmsType.valueOf(field.value().trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default SaveRecordRequest toSaveRecordRequest(ClientRequest request) {
        return new SaveRecordRequest(
                "CRUDServiceProvider.saveRecord",
                new SaveRecordRequest.RequestBody(
                        new SaveRecordRequest.DataSet(
                                "Parceiro",
                                "S",
                                new SaveRecordRequest.DataRow(
                                        new SaveRecordRequest.LocalFields(
                                                new SaveRecordRequest.SmField(request.cgcCpf()),
                                                new SaveRecordRequest.SmField(request.nomeParc()),
                                                new SaveRecordRequest.SmField(request.razaoSocial() != null ? request.razaoSocial() : ""),
                                                new SaveRecordRequest.SmField(PersonType.valueOf(request.personType()).getSankhyaCode()),
                                                new SaveRecordRequest.SmField(request.classificms()),
                                                new SaveRecordRequest.SmField(String.valueOf(request.codCid()))
                                        )
                                ),
                                new SaveRecordRequest.Entity(
                                        new SaveRecordRequest.FieldSet("CGC_CPF,NOMEPARC,RAZAOSOCIAL,TIPPESSOA,CLASSIFICMS,CODCID")
                                )
                        )
                )
        );
    }
}
