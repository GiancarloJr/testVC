package com.br.vidya.dto.request;

import com.br.vidya.model.enums.PersonType;
import jakarta.validation.constraints.*;

public record ClientRequest(

        @NotBlank(message = "Partner name cannot be empty")
        @Size(max = 255, message = "Partner name must not exceed 255 characters")
        String nomeParc,

        @Size(max = 255, message = "Corporate name must not exceed 255 characters")
        String razaoSocial,

        @NotBlank(message = "CGC/CPF cannot be empty")
        @Size(max = 20, message = "CGC/CPF must not exceed 20 characters")
        String cgcCpf,

        @NotBlank(message = "Person type cannot be null")
        @Pattern(
                regexp = "^(FISICA|JURIDICA)$",
                message = "personType must be either FISICA or JURIDICA"
        )
        String personType,

        @NotNull(message = "City code cannot be null")
        @PositiveOrZero(message = "City code must be a positive number")
        Long codCid,

        @NotBlank(message = "ICMS classification cannot be empty")
        String classificms
) {}
