package com.iasdietideals24.dietideals24.utilities.dto.shallows;

public class AstaShallowDto {

    private Long idAsta = 0L;

    private String tipoAstaPerAccount = "";

    private String tipoAstaSpecifica = "";

    public AstaShallowDto(Long idAsta, String tipoAstaPerAccount, String tipoAstaSpecifica) {
        this.idAsta = idAsta;
        this.tipoAstaPerAccount = tipoAstaPerAccount;
        this.tipoAstaSpecifica = tipoAstaSpecifica;
    }

    public AstaShallowDto() {
    }

    public Long getIdAsta() {
        return this.idAsta;
    }

    public void setIdAsta(Long idAsta) {
        this.idAsta = idAsta;
    }

    public String getTipoAstaPerAccount() {
        return this.tipoAstaPerAccount;
    }

    public String getTipoAstaSpecifica() {
        return this.tipoAstaSpecifica;
    }
}
