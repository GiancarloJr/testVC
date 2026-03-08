package com.br.vidya.integration.receita.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CnpjUtils {

    public String sanitize(String cnpj) {
        return cnpj.replaceAll("[^0-9]", "").trim();
    }
}
