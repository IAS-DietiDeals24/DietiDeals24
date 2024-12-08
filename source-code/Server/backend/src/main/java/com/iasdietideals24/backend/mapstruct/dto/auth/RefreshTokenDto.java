package com.iasdietideals24.backend.mapstruct.dto.auth;

public record RefreshTokenDto(String authToken, Long expiresIn) {
}
