package com.iasdietideals24.backend.mapstruct.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CognitoTokenResponseDto(String id_token, String refresh_token, Long expires_in) {
}
