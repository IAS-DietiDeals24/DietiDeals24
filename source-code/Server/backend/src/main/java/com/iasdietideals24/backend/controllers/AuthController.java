package com.iasdietideals24.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iasdietideals24.backend.exceptions.AuthRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.auth.CognitoTokenResponseDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.NewTokenDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.RefreshTokenDto;
import com.iasdietideals24.backend.mapstruct.dto.auth.UrlDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Slf4j
@RestController
public class AuthController {

    public static final String IMPOSSIBILE_INVIARE_COGNITO_REQUEST = "Impossibile inviare la Cognito request";
    public static final String LOG_REDIRECT_URI_RICEVUTO = "Redirect URI ricevuto: {}";
    public static final String LOG_LETTURA_COGNITO_RESPONSE_FALLITA = "Lettura della Cognito response fallita";

    private static final String DEFAULT_REDIRECT_URI = "https://d84l1y8p4kdic.cloudfront.net";

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Value("${spring.security.oauth2.resourceserver.jwt.clientId}") // Leggiamo il valore dall'application.properties
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.jwt.clientSecret}") // Leggiamo il valore dall'application.properties
    private String clientSecret;

    @Value("${auth.cognitoUri}") // Leggiamo il valore dall'application.properties
    private String cognitoUri;

    // No authentication required
    // Generiamo l'URL a cui il frontend deve accedere per fare il login su Cognito
    // Una volta fatto il login, il frontend avrà un Codice che servirà per l'autenticazione
    @GetMapping("/auth/url")
    public ResponseEntity<UrlDto> auth(@RequestParam(name = "redirect_uri", defaultValue = DEFAULT_REDIRECT_URI) String redirectUri) {

        log.debug("Costruisco l'URL...");

        log.trace(LOG_REDIRECT_URI_RICEVUTO, redirectUri);

        // Potrebbe essere necessario cambiare l'url generato (lo scope o redirect_uri) in base al client API
        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/authorization-endpoint.html
        String url = cognitoUri +
                "/oauth2/authorize?" +
                "client_id=" + clientId +
                "&response_type=code" +
                "&scope=email+openid+phone" +
                "&redirect_uri=" + redirectUri;

        log.debug("URL costruito. Invio in corso...");

        return new ResponseEntity<>(new UrlDto(url), HttpStatus.OK);
    }

    // No authentication required
    // Validiamo il Codice ricevuto dal frontend e restituiamo il JWT necessario per l'autenticazione, il refresh token e l'expiration time in millisecondi
    // Il JWT dovrà essere inserito nell'header di ogni richiesta che necessita di autorizzazione
    @GetMapping("/auth/callback")
    public ResponseEntity<NewTokenDto> callback(@RequestParam("code") String code,
                                                @RequestParam(name = "redirect_uri", defaultValue = DEFAULT_REDIRECT_URI) String redirectUri) {

        log.info("Autenticazione in corso...");

        log.trace("Codice ricevuto: {}", code);
        log.trace(LOG_REDIRECT_URI_RICEVUTO, redirectUri);

        // Creiamo l'URI per l'autenticazione del codice tramite Cognito
        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html
        String urlStr = cognitoUri + "/oauth2/token?" +
                "grant_type=authorization_code" +
                "&code=" + code +
                "&redirect_uri=" + redirectUri;

        HttpRequest request = buildCognitoRequest(urlStr);
        HttpResponse<String> response = sendCognitoRequest(request);

        if (response.statusCode() != 200) {
            log.warn("Autenticazione fallita");
            throw new AuthRuntimeException("Autenticazione fallita");
        }

        log.info("Autenticazione riuscita.");

        log.info("Lettura della Cognito response in corso...");

        // Leggiamo la riposta di Cognito
        CognitoTokenResponseDto cognitoTokenResponse;
        try {
            cognitoTokenResponse = JSON_MAPPER.readValue(response.body(), CognitoTokenResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn(LOG_LETTURA_COGNITO_RESPONSE_FALLITA);
            throw new AuthRuntimeException(LOG_LETTURA_COGNITO_RESPONSE_FALLITA);
        }

        log.trace("cognitoTokenResponse: {}", cognitoTokenResponse);

        log.info("Cognito response letta correttamente. Invio in corso...");

        return new ResponseEntity<>(new NewTokenDto(cognitoTokenResponse.id_token(), cognitoTokenResponse.refresh_token(), cognitoTokenResponse.expires_in()), HttpStatus.OK);
    }

    // No authentication required
    // Restituiamo un nuovo JWT per l'autenticazione in base al refresh_token passato per parametro
    // Il JWT dovrà essere inserito nell'header di ogni richiesta che necessita di autorizzazione
    @GetMapping("/auth/refresh")
    public ResponseEntity<RefreshTokenDto> refresh(@RequestParam("refresh_token") String refreshToken) {

        log.info("Generazione di un nuovo JWT in corso...");

        log.trace("Refresh token ricevuto: {}", refreshToken);

        // Creiamo l'URI per l'autenticazione del codice tramite Cognito
        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html
        String urlStr = cognitoUri + "/oauth2/token?" +
                "grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        HttpRequest request = buildCognitoRequest(urlStr);
        HttpResponse<String> response = sendCognitoRequest(request);

        if (response.statusCode() != 200) {
            log.warn("Generazione del JWT fallita");
            throw new AuthRuntimeException("Generazione del JWT fallita");
        }

        log.info("Generazione del JWT riuscita.");

        log.info("Lettura della Cognito response in corso...");

        // Leggiamo la riposta di Cognito
        CognitoTokenResponseDto cognitoTokenResponse;
        try {
            cognitoTokenResponse = JSON_MAPPER.readValue(response.body(), CognitoTokenResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn(LOG_LETTURA_COGNITO_RESPONSE_FALLITA);
            throw new AuthRuntimeException(LOG_LETTURA_COGNITO_RESPONSE_FALLITA);
        }

        log.trace("cognitoTokenResponse: {}", cognitoTokenResponse);

        log.info("Cognito response letta correttamente. Invio in corso...");

        return new ResponseEntity<>(new RefreshTokenDto(cognitoTokenResponse.id_token(), cognitoTokenResponse.expires_in()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    // Generiamo l'URL a cui il frontend deve accedere per fare il logout da Cognito
    @GetMapping("/auth/logout")
    public ResponseEntity<UrlDto> logout(@RequestParam(name = "logout_uri", required = false) String logoutUri,
                                         @RequestParam(name = "redirect_uri", defaultValue = DEFAULT_REDIRECT_URI) String redirectUri,
                                         @RequestParam("refresh_token") String refreshToken) {

        log.info("Disconnessione in corso...");

        log.trace("Logout URI ricevuto: {}", logoutUri);
        log.trace(LOG_REDIRECT_URI_RICEVUTO, redirectUri);
        log.trace("Refresh token ricevuto: {}", refreshToken);

        // Creiamo l'URI per l'autenticazione del codice tramite Cognito
        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/revocation-endpoint.html
        String urlStr = cognitoUri + "/oauth2/revoke?" +
                "&token=" + refreshToken;

        HttpRequest request = buildCognitoRequest(urlStr);
        HttpResponse<String> response = sendCognitoRequest(request);

        if (response.statusCode() != 200) {
            log.warn("Disconnessione fallita");
            throw new AuthRuntimeException("Disconnessione fallita");
        }

        log.info("Disconnessione riuscita.");

        log.debug("Costruisco l'URL...");

        // Documentazione: https://docs.aws.amazon.com/cognito/latest/developerguide/logout-endpoint.html
        String url = cognitoUri +
                "/logout?" +
                "client_id=" + clientId +
                "&response_type=code" +
                "&scope=email+openid+phone" +
                "&redirect_uri=" + redirectUri;

        if (logoutUri != null)
            url = url + "&logout_uri=" + logoutUri;

        log.debug("URL costruito. Invio in corso...");

        return new ResponseEntity<>(new UrlDto(url), HttpStatus.OK);
    }

    private HttpRequest buildCognitoRequest(String urlStr) {

        log.debug("Costruisco il pacchetto da inviare ad AWS Cognito...");

        log.trace("URL Cognito request: {}", urlStr);

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
            log.warn("Validazione del Cognito URL non riuscita");
            throw new AuthRuntimeException("Validazione del Cognito URL non riuscita");
        }

        log.trace("request: {}", request);

        log.debug("Cognito request costruita.");

        return request;
    }

    private HttpResponse<String> sendCognitoRequest(HttpRequest request) {

        log.debug("Invio della Cognito request in corso...");

        // Creiamo il client che manderà il pacchetto di richiesta ad AWS Cognito
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

        log.trace("Cognito response: Status code: '{}'; Body: {}", response.statusCode(), response.body());
        log.debug("Cognito request inviata.");

        return response;
    }
}