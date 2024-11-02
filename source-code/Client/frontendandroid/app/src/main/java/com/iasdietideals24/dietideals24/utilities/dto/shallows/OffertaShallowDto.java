package com.iasdietideals24.dietideals24.utilities.dto.shallows;

public class OffertaShallowDto {

    private Long idOfferta = 0L;

    private String tipoOffertaPerAccount = "";

    private String tipoOffertaSpecifica = "";

    public Long getIdOfferta() {
        return this.idOfferta;
    }

    public void setIdOfferta(Long idOfferta) {
        this.idOfferta = idOfferta;
    }

    public String getTipoOffertaPerAccount() {
        return this.tipoOffertaPerAccount;
    }

    public String getTipoOffertaSpecifica() {
        return this.tipoOffertaSpecifica;
    }
}
