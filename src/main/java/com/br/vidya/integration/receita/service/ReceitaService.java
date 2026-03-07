package com.br.vidya.integration.receita.service;

import com.br.vidya.integration.receita.dto.ReceitaResponse;
import com.br.vidya.integration.receita.gateway.ReceitaGateway;
import com.br.vidya.integration.receita.utils.CnpjUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ReceitaService {

    private final ReceitaGateway receitaGateway;

    public ReceitaResponse findByCnpj(String cnpj) {
        log.info("Looking up CNPJ: {}", cnpj);

        String sanitizedCnpj = CnpjUtils.sanitize(cnpj);

        return receitaGateway.get(ReceitaResponse.class, "Receita API Error", sanitizedCnpj);
    }
}
