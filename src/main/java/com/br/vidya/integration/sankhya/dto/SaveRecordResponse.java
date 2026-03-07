package com.br.vidya.integration.sankhya.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SaveRecordResponse(
        String serviceName,
        String status,
        String pendingPrinting,
        String transactionId,
        String statusMessage,
        ResponseBody responseBody
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResponseBody(
            Entities entities
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Entities(
            String total,
            Entity entity
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Entity(
            @JsonProperty("NOMEPARC") SmField nomeParc,
            @JsonProperty("CODPARC") SmField codParc,
            @JsonProperty("CGC_CPF") SmField cgcCpf
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SmField(
            @JsonProperty("$")
            String value
    ) {}
}
