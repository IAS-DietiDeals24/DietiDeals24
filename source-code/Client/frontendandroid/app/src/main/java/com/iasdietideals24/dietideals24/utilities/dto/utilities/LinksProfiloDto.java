package com.iasdietideals24.dietideals24.utilities.dto.utilities;

public class LinksProfiloDto {

    private String linkPersonale = "";

    private String linkInstagram = "";

    private String linkFacebook = "";

    private String linkGitHub = "";

    private String linkX = "";

    public LinksProfiloDto(String linkPersonale, String linkInstagram, String linkFacebook, String linkGitHub, String linkX) {
        this.linkPersonale = linkPersonale;
        this.linkInstagram = linkInstagram;
        this.linkFacebook = linkFacebook;
        this.linkGitHub = linkGitHub;
        this.linkX = linkX;
    }

    public LinksProfiloDto() {
    }

    public String getLinkPersonale() {
        return this.linkPersonale;
    }

    public String getLinkInstagram() {
        return this.linkInstagram;
    }

    public String getLinkFacebook() {
        return this.linkFacebook;
    }

    public String getLinkGitHub() {
        return this.linkGitHub;
    }

    public String getLinkX() {
        return this.linkX;
    }
}
