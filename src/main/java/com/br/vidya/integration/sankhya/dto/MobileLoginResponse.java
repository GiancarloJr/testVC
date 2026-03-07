package com.br.vidya.integration.sankhya.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MobileLoginResponse(
        String serviceName,
        String status,
        boolean pendingPrinting,
        String transactionId,
        ResponseBody responseBody
) {
    public record ResponseBody(
            SmField callID,
            SmField jsessionid,
            SmField kID,
            SmField idusu
    ) {}

    public record SmField(
            @JsonProperty("$")
            String value
    ) {}
}
