package com.br.vidya.integration.sankhya.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MobileLoginRequest(
        String serviceName,
        RequestBody requestBody
) {
    public record RequestBody(
            SmField NOMUSU,
            SmField INTERNO,
            SmField KEEPCONNECTED
    ) {}

    public record SmField(
            @JsonProperty("$")
            String value
    ) {}
}
