package com.iasdietideals24.dietideals24.utilities.dto.utilities;

public class TokensAccountDto {

    private String idFacebook = "";

    private String idGoogle = "";

    private String idX = "";

    private String idGitHub = "";

    public TokensAccountDto(String idFacebook, String idGoogle, String idX, String idGitHub) {
        this.idFacebook = idFacebook;
        this.idGoogle = idGoogle;
        this.idX = idX;
        this.idGitHub = idGitHub;
    }

    public TokensAccountDto() {
    }

    public String getIdFacebook() {
        return this.idFacebook;
    }

    public String getIdGoogle() {
        return this.idGoogle;
    }

    public String getIdX() {
        return this.idX;
    }

    public String getIdGitHub() {
        return this.idGitHub;
    }
}
