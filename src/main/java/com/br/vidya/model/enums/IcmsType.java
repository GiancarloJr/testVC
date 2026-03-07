package com.br.vidya.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum IcmsType {
    C(1);

    private final Integer code;

    public static IcmsType fromCode(int code) {
        return Arrays.stream(values())
                .filter(d -> d.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid DocumentType code: " + code));
    }
}
