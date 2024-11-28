package com.iasdietideals24.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iasdietideals24.backend.mapstruct.dto.auth.CognitoTokenResponseDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.TokenDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.UrlDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * Il controller "AuthController" si occupa di gestire l'autenticazione di un nuovo utente tramite AWS Cognito.
 */

@RestController
public class AuthController {

    @Value("${spring.security.oauth2.resourceserver.jwt.clientId}") // Leggiamo il valore dall'application.properties
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.jwt.clientSecret}") // Leggiamo il valore dall'application.properties
    private String clientSecret;

    @Value("${auth.cognitoUri}") // Leggiamo il valore dall'application.properties
    private String cognitoUri;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    // Generiamo l'URL a cui il frontend deve accedere per fare il login su Cognito
    @GetMapping("/auth/url")
    public ResponseEntity<UrlDto> auth() {

        String url = cognitoUri +
                "/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&scope=email+openid+profile";

        return new ResponseEntity<>(new UrlDto(url), HttpStatus.OK);
    }

    // Validiamo il codice e restituiamo il JWT
    @GetMapping("/auth/callback")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) {

        // Creiamo l'URI di Cognito
        String urlStr = cognitoUri + "/oauth2/token?"
                + "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&code=" + code;

        // Recuperiamo l'ID e Secret per autenticare il backend in Cognito
        String authenticationInfo = clientId + ":" + clientSecret;
        String basicAuthenticationInfo = Base64.getEncoder().encodeToString(authenticationInfo.getBytes()); // Codifichiamo in Base64

        // Creiamo il pacchetto della richiesta di autenticazione del nuovo utente a Cognito
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(new URI(urlStr))
                    .header("Content-type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + basicAuthenticationInfo)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to build Cognito URL");
        }

        // Creiamo il client che manderà il pacchetto di richiesta di autenticazione del nuovo utente
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response; // Qui salveremo la risposta di Cognito
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Mandiamo la richiesta a Cognito
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Unable to request Cognito");
        }

        if (response.statusCode() != 200) {
            throw new RuntimeException("Authentication failed");
        }

        // Leggiamo la riposta di Cognito
        CognitoTokenResponseDto token;
        try {
            token = JSON_MAPPER.readValue(response.body(), CognitoTokenResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to decode Cognito response");
        }

        return new ResponseEntity<>(new TokenDto(token.id_token()), HttpStatus.OK);
    }
}