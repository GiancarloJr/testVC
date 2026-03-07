package com.br.vidya.integration.sankhya.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceName {

    MOBILE_LOGIN("MobileLoginSP.login"),
    LOAD_RECORDS("CRUDServiceProvider.loadRecords"),
    SAVE_RECORD("CRUDServiceProvider.saveRecord");

    private final String serviceName;
}
