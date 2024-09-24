package com.iasdietideals24.dietideals24.utilities.dto.utilities;

public class TokensAccountDto {

    private String idFacebook;

    private String idGoogle;

    private String idX;

    private String idGitHub;

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

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public String getIdGoogle() {
        return this.idGoogle;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
    }

    public String getIdX() {
        return this.idX;
    }

    public void setIdX(String idX) {
        this.idX = idX;
    }

    public String getIdGitHub() {
        return this.idGitHub;
    }

    public void setIdGitHub(String idGitHub) {
        this.idGitHub = idGitHub;
    }
}
