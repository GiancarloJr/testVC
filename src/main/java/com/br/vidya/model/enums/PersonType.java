package com.br.vidya.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PersonType {

    FISICA(1, "F"),
    JURIDICA(2, "J");

    private final int code;
    private final String sankhyaCode;

    public static PersonType fromCode(int code) {
        return Arrays.stream(values())
                .filter(p -> p.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid PersonType code: " + code));
    }

    public static PersonType fromSankhyaCode(String sankhyaCode) {
        return Arrays.stream(values())
                .filter(p -> p.sankhyaCode.equalsIgnoreCase(sankhyaCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid PersonType sankhya code: " + sankhyaCode));
    }
}
