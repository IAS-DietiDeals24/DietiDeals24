package com.iasdietideals24.backend.entities.utilities;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class TokensAccount {

    private String idFacebook;

    private String idGoogle;

    private String idX;

    private String idGitHub;

    // AllArgsConstructor
    public TokensAccount(String idFacebook, String idGoogle, String idX, String idGitHub) {
        this.idFacebook = idFacebook;
        this.idGoogle = idGoogle;
        this.idX = idX;
        this.idGitHub = idGitHub;
    }

    public String toString() {
        return "TokensAccount(idFacebook=" + this.getIdFacebook() + ", idGoogle=" + this.getIdGoogle() + ", idX=" + this.getIdX() + ", idGitHub=" + this.getIdGitHub() + ")";
    }
}
