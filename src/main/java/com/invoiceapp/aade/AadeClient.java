package com.invoiceapp.aade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Component
public class AadeClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public AadeClient(@Value("${aade.api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public AadeCompanyDto fetchCompany(String accessToken) {
        String url = baseUrl + "/companies/me"; // τώρα χρησιμοποιεί το property

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AadeCompanyDto> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, AadeCompanyDto.class);

        return response.getBody();
    }
}
