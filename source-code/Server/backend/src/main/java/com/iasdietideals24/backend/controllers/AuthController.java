package com.iasdietideals24.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iasdietideals24.backend.exceptions.AuthRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.auth.CognitoTokenResponseDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.TokenDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.UrlDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;
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

@Slf4j
@RestController
public class AuthController {

    public static final String IMPOSSIBILE_INVIARE_COGNITO_REQUEST = "Impossibile inviare la Cognito request";

    @Value("${spring.security.oauth2.resourceserver.jwt.clientId}") // Leggiamo il valore dall'application.properties
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.jwt.clientSecret}") // Leggiamo il valore dall'application.properties
    private String clientSecret;

    @Value("${auth.cognitoUri}") // Leggiamo il valore dall'application.properties
    private String cognitoUri;

    private final String defaultRedirectUrl = "https://d84l1y8p4kdic.cloudfront.net";

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    // Generiamo l'URL a cui il frontend deve accedere per fare il login su Cognito
    // Una volta fatto il login, il frontend avrà un Codice che servirà per l'autenticazione
    @GetMapping("/auth/url")
    public ResponseEntity<UrlDto> auth(@RequestParam(defaultValue = defaultRedirectUrl) String redirectUrl) {

        log.debug("Costruisco l'URL...");

        // Potrebbe essere necessario cambiare l'url generato (lo scope o redirect_uri) in base al client API
        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/authorization-endpoint.html
        String url = cognitoUri +
                "/oauth2/authorize?" +
                "client_id=" + clientId +
                "&response_type=code" +
                "&scope=email+openid+phone" +
                "&redirect_uri=" + redirectUrl;

        log.debug("URL costruito. Invio in corso...");

        return new ResponseEntity<>(new UrlDto(url), HttpStatus.OK);
    }

    // Validiamo il Codice ricevuto dal frontend e restituiamo il JWT
    // Il JWT dovrà essere inserito nell'header di ogni richiesta che necessita di autorizzazione
    @GetMapping("/auth/callback")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) {

        log.info("Autenticazione in corso...");

        log.trace("Codice ricevuto: {}", code);

        log.debug("Costruisco il pacchetto da inviare ad AWS Cognito...");

        // Creiamo l'URI per l'autenticazione del codice tramite Cognito
        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html
        String urlStr = cognitoUri + "/oauth2/token?" +
                "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&code=" + code +
                "&redirect_uri=https://d84l1y8p4kdic.cloudfront.net";

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
            log.warn("Costruzione del Cognito URL non riuscita");

            throw new AuthRuntimeException("Costruzione del Cognito URL non riuscita");
        }

        log.debug("Cognito request costruita. Invio in corso...");

        // Creiamo il client che manderà il pacchetto di richiesta di autenticazione del nuovo utente
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response; // Qui salveremo la risposta di Cognito
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Mandiamo la richiesta a Cognito
        } catch (IOException e) {
            log.warn(IMPOSSIBILE_INVIARE_COGNITO_REQUEST);

            throw new AuthRuntimeException(IMPOSSIBILE_INVIARE_COGNITO_REQUEST);
        } catch (InterruptedException e) {
            log.warn(IMPOSSIBILE_INVIARE_COGNITO_REQUEST);

            Thread.currentThread().interrupt();
            throw new AuthRuntimeException(IMPOSSIBILE_INVIARE_COGNITO_REQUEST);
        }

        log.debug("Cognito request inviata.");

        log.trace("Cognito response: Status code: '{}'; Body: {}", response.statusCode(), response.body());

        if (response.statusCode() != 200) {
            log.warn("Autenticazione fallita");

            throw new AuthRuntimeException("Autenticazione fallita");
        }

        log.info("Autenticazione riuscita.");

        log.info("Lettura della Cognito response in corso...");

        // Leggiamo la riposta di Cognito
        CognitoTokenResponseDto token;
        try {
            token = JSON_MAPPER.readValue(response.body(), CognitoTokenResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn("Lettura della Cognito response fallita");

            throw new AuthRuntimeException("Lettura della Cognito response fallita");
        }

        log.info("Cognito response letta correttamente. Invio in corso...");

        return new ResponseEntity<>(new TokenDto(token.id_token()), HttpStatus.OK);
    }
}