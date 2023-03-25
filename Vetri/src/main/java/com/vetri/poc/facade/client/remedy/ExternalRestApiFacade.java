package com.vetri.poc.facade.client.remedy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vetri.poc.facade.config.JwtTokenUtil;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class ExternalRestApiFacade {

    RestTemplate restTemplate;

    private JwtTokenUtil jwtTokenUtil;

    private String user;

    private String password;

    private static final String BASE_URL = "http://localhost:8080/";
    private static final String AUTHENTICATION_PATH = "authenticate";
    private static final String HELLO_PATH = "hello";

    private String token;
    private Date tokenExpiration;

    public ExternalRestApiFacade(String user, String password,RestTemplate restTemplate,JwtTokenUtil jwtTokenUtil) {
        this.user=user;
        this.password=password;
        this.restTemplate=restTemplate;
        this.jwtTokenUtil=jwtTokenUtil;
        // Perform authentication and obtain JWT token
        authenticate();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getToken());
        return headers;
    }

    private void authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBasicAuth(clientId, clientSecret);

        // Build request body with username and password
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", user);
        requestBody.put("password", password);

        HttpEntity<Object> entity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(BASE_URL+AUTHENTICATION_PATH, HttpMethod.POST, entity, TokenResponse.class);
        TokenResponse tokenResponse = responseEntity.getBody();
        token = tokenResponse.getToken();
        tokenExpiration = jwtTokenUtil.getExpirationDateFromToken(token);
    }

    private String getToken() {
        if (token == null || isTokenExpired()) {
            authenticate();
        }
        return token;
    }

    private boolean isTokenExpired() {
        return this.tokenExpiration != null && new Date().after(this.tokenExpiration);
    }

    public String getHelloResource(){
        ResponseEntity<String> responseEntity = get(HELLO_PATH);
        return responseEntity.getBody();
    }

    public ResponseEntity<String> get(String path) {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(BASE_URL + path, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> post(String path, Object requestBody) {
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(BASE_URL + path, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> put(String path, Object requestBody) {
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(BASE_URL + path, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<String> delete(String path) {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(BASE_URL + path, HttpMethod.DELETE, entity, String.class);
    }

    @Getter
    @Setter
    private static class TokenResponse {
        @JsonProperty("token")
        private String token;
    }

}
