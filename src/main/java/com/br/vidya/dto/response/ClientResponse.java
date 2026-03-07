package com.br.vidya.dto.response;

import com.br.vidya.model.enums.PersonType;

public record ClientResponse(
        Long codParc,
        String nomeParc,
        String razaoSocial,
        String cgcCpf,
        PersonType personType,
        String classificms,
        Long codCid,
        String cityName
) {}
