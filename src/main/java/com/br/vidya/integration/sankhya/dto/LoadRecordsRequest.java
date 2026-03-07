package com.br.vidya.integration.sankhya.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LoadRecordsRequest(
        String serviceName,
        RequestBody requestBody
) {
    public record RequestBody(
            DataSet dataSet
    ) {}

    public record DataSet(
            String rootEntity,
            String includePresentationFields,
            String offsetPage,
            Criteria criteria,
            Entity entity
    ) {}
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Criteria(
            Expression expression,
            List<Parameter> parameter
    ) {
        public Criteria(String expression) {
            this(new Expression(expression),null);
        }
    }

    public record Expression(
            @JsonProperty("$")
            String value
    ) {}

    public record Parameter(
            String type,
            String $
    ) {}

    public record Entity(
            FieldSet fieldset
    ) {}

    public record FieldSet(
            String list
    ) {}
}
