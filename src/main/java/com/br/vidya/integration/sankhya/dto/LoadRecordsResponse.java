package com.br.vidya.integration.sankhya.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoadRecordsResponse(
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
            String hasMoreResult,
            String offsetPage,
            String offset,
            Metadata metadata,
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            List<Entity> entity
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Metadata(
            Fields fields
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Fields(
            List<Field> field
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Field(
            String name
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Entity(
            SmField f0,
            SmField f1,
            SmField f2,
            SmField f3,
            SmField f4,
            SmField f5,
            SmField f6,
            SmField f7
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SmField(
            @JsonProperty("$")
            String value
    ) {}
}