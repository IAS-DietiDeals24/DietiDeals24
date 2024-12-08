package com.iasdietideals24.dietideals24.utilities.dto.utilities;

public class RefreshTokenDto {
    private String authToken = "";

    private Long expiresIn = 0L;

    public RefreshTokenDto() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
