package com.br.vidya.integration.sankhya.service;

import com.br.vidya.integration.sankhya.dto.LoadRecordsRequest;
import com.br.vidya.integration.sankhya.exceptions.SankhyaApiException;
import com.br.vidya.integration.sankhya.exceptions.SankhyaAuthorizationException;
import lombok.extern.slf4j.Slf4j;

import static com.br.vidya.integration.sankhya.enums.ServiceName.LOAD_RECORDS;

@Slf4j
public abstract class SankhyaAbstractService {

    protected LoadRecordsRequest buildLoadRecordsRequest(String entityName, String offSet, String fields, LoadRecordsRequest.Criteria criteria) {
        return new LoadRecordsRequest(
                LOAD_RECORDS.getServiceName(),
                new LoadRecordsRequest.RequestBody(
                        new LoadRecordsRequest.DataSet(
                                entityName,
                                "S",
                                offSet != null ? offSet : "0",
                                criteria,
                                new LoadRecordsRequest.Entity(
                                        new LoadRecordsRequest.FieldSet(fields)
                                )
                        )
                )
        );
    }

    protected void validateCodeResponse(String className,String code, String message) {
        switch (code) {
            case "3" -> throw new SankhyaAuthorizationException(message);
            case "0" -> throw new SankhyaApiException(message);
            case "1" -> log.info("Sankhya API response successful - Service: {}", className);
            default -> log.error("Unknown code: {}", code);
        }

    }

}
