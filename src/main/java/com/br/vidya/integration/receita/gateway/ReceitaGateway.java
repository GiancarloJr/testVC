package com.br.vidya.integration.receita.gateway;

import com.br.vidya.integration.receita.exceptions.ReceitaException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReceitaGateway {

    private final RestClient receitaRestClient;

    public ReceitaGateway(@Qualifier("receitaWsRestClient") RestClient receitaWsRestClient) {
        this.receitaRestClient = receitaWsRestClient;
    }

    public <T> T get(Class<T> responseType, String errorMessage, String cnpj) {
        return receitaRestClient.get()
                .uri("/v1/cnpj/{cnpj}", cnpj)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    if(res.getStatusCode().is4xxClientError()) {
                        throw new ReceitaException(
                                "CNPJ not found or invalid: " + cnpj, res.getStatusCode()
                        );
                    }
                    throw new ReceitaException(errorMessage, res.getStatusCode());
                })
                .body(responseType);
    }
}
