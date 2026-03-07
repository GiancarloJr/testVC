package com.br.vidya.integration.sankhya.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityName {

    PARTNER("Parceiro"),
    CITY("Cidade");

    private final String entityName;
}
