package com.iasdietideals24.backend.entities.utilities;

import jakarta.persistence.Column;
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

    @Column(name = "id_facebook")
    private String idFacebook;

    @Column(name = "id_google")
    private String idGoogle;

    @Column(name = "id_x")
    private String idX;

    @Column(name = "id_git_hub")
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
