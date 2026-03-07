package com.br.vidya.integration.sankhya.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaveRecordRequest(
    String serviceName,
    RequestBody requestBody
) {
        public record RequestBody(
                DataSet dataSet
        ) {}

        public record DataSet(
                String rootEntity,
                String includePresentationFields,
                DataRow dataRow,
                Entity entity
        ) {}

        public record DataRow(
                LocalFields localFields
        ) {}

        public record LocalFields(
                @JsonProperty("CGC_CPF") SmField cgcCpf,
                @JsonProperty("NOMEPARC") SmField nomeParc,
                @JsonProperty("RAZAOSOCIAL") SmField razaoSocial,
                @JsonProperty("TIPPESSOA") SmField tipPessoa,
                @JsonProperty("CLASSIFICMS") SmField classificms,
                @JsonProperty("CODCID") SmField codCid
        ) {}

        public record Entity(
                FieldSet fieldset
        ) {}

        public record FieldSet(
                String list
        ) {}

        public record SmField(
                @JsonProperty("$")
                String value
        ) {}
    }
