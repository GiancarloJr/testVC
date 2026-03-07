package com.br.vidya.integration.sankhya.gateway;

import com.br.vidya.integration.sankhya.exceptions.SankhyaApiException;
import com.br.vidya.integration.sankhya.service.SankhyaAuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SankhyaGateway {

    private final RestClient restClient;
    private final SankhyaAuthService authService;
    private final String urlBaseServices;

    public SankhyaGateway(
            @Qualifier("sankhyaRestClient") RestClient restClient,
            SankhyaAuthService authService,
            @Value("${sankhya.url-base-services}") String urlBaseServices
    ) {
        this.restClient = restClient;
        this.authService = authService;
        this.urlBaseServices = urlBaseServices;
    }

    public <T> T post(Object request, String serviceName, Class<T> responseType, String errorMessage) {

        try {
            return restClient.post()
                    .uri(urlBaseServices, serviceName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Cookie", "JSESSIONID=" + authService.getSessionId())
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new SankhyaApiException(
                                errorMessage + " | service=" + serviceName
                        );
                    })
                    .body(responseType);
        } catch (Exception e) {
            throw new SankhyaApiException("Erro na api do Sankhya: " + e.getMessage());
        }
    }
}