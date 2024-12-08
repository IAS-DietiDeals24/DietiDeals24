package com.iasdietideals24.dietideals24.utilities.dto.utilities;

public class NewTokenDto {
    private String authToken = "";

    private String refreshToken = "";

    private Long expiresIn = 0L;

    public NewTokenDto() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
