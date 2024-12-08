package com.iasdietideals24.backend.mapstruct.dto.auth;

public record NewTokenDto(String authToken, String refreshToken, Long expiresIn) {
}
