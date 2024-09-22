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
public class LinksProfilo {

    private String linkPersonale;

    private String linkInstagram;

    private String linkFacebook;

    private String linkGitHub;

    private String linkX;

    // AllArgsConstructor
    public LinksProfilo(String linkPersonale, String linkInstagram, String linkFacebook, String linkGitHub, String linkX) {
        this.linkPersonale = linkPersonale;
        this.linkInstagram = linkInstagram;
        this.linkFacebook = linkFacebook;
        this.linkGitHub = linkGitHub;
        this.linkX = linkX;
    }

    public String toString() {
        return "LinksProfilo(linkPersonale=" + this.getLinkPersonale() + ", linkInstagram=" + this.getLinkInstagram() + ", linkFacebook=" + this.getLinkFacebook() + ", linkGitHub=" + this.getLinkGitHub() + ", linkX=" + this.getLinkX() + ")";
    }
}
