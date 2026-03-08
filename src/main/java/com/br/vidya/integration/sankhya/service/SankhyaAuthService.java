package com.br.vidya.integration.sankhya.service;

import com.br.vidya.integration.sankhya.dto.MobileLoginRequest;
import com.br.vidya.integration.sankhya.dto.MobileLoginResponse;
import com.br.vidya.integration.sankhya.exceptions.SankhyaApiException;
import com.br.vidya.integration.sankhya.exceptions.SankhyaAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.br.vidya.integration.sankhya.enums.ServiceName.MOBILE_LOGIN;

@Slf4j
@Service
public class SankhyaAuthService {

    private final RestClient sankhyaRestClient;
    private final String username;
    private final String password;
    private volatile String jsessionId;
    private final String urlLogin;

    public SankhyaAuthService(@Qualifier("sankhyaRestClient") RestClient sankhyaRestClient,
                              @Value("${sankhya.username}") String username,
                              @Value("${sankhya.password}") String password,
                              @Value("${sankhya.url-login}") String urlLogin
    ) {
        this.sankhyaRestClient = sankhyaRestClient;
        this.username = username;
        this.password = password;
        this.urlLogin = urlLogin;
    }

    public synchronized String getSessionId() {
        if (jsessionId == null) {
            login();
        }
        return jsessionId;
    }

    public synchronized void refreshSession() {
        jsessionId = null;
        login();
    }

    private void login() {
        log.info("Authenticating with Sankhya ERP API");

        MobileLoginRequest request = buildLoginRequest();
        MobileLoginResponse response = loginRequest(request);

        validateLogin(response);

        jsessionId = response.responseBody().jsessionid().value();

        log.info("Sankhya authentication successful. Session ID obtained.");
    }

    private MobileLoginRequest buildLoginRequest() {
        return new MobileLoginRequest(
                MOBILE_LOGIN.getServiceName(),
                new MobileLoginRequest.RequestBody(
                        new MobileLoginRequest.SmField(username),
                        new MobileLoginRequest.SmField(password),
                        new MobileLoginRequest.SmField("N")
                )
        );
    }

    private MobileLoginResponse loginRequest(MobileLoginRequest request) {
        try {
            return sankhyaRestClient.post()
                    .uri(urlLogin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new SankhyaAuthenticationException("Sankhya login failed");
                    })
                    .body(MobileLoginResponse.class);

        } catch (Exception e) {
            throw new SankhyaApiException("Error during Sankhya login: " + e.getMessage());
        }

    }

    private void validateLogin(MobileLoginResponse response) {
        if (response == null ||
                response.responseBody() == null ||
                response.responseBody().jsessionid() == null) {
            throw new SankhyaApiException("Sankhya login failed");
        }
    }
}
