package com.iasdietideals24.dietideals24.utilities.dto.shallows;

public class AccountShallowDto {

    private Long idAccount = 0L;

    private String tipoAccount = "";

    public AccountShallowDto(Long idAccount, String tipoAccount) {
        this.idAccount = idAccount;
        this.tipoAccount = tipoAccount;
    }

    public AccountShallowDto() {
    }

    public Long getIdAccount() {
        return this.idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public String getTipoAccount() {
        return this.tipoAccount;
    }

    public void setTipoAccount(String tipoAccount) {
        this.tipoAccount = tipoAccount;
    }
}
