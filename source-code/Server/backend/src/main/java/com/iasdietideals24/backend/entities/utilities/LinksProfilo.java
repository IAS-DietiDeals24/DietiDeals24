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
public class LinksProfilo {

    @Column(name = "link_personale")
    private String linkPersonale;

    @Column(name = "link_instagram")
    private String linkInstagram;

    @Column(name = "link_facebook")
    private String linkFacebook;

    @Column(name = "link_git_hub")
    private String linkGitHub;

    @Column(name = "link_x")
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
