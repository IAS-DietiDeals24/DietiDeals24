package com.iasdietideals24.dietideals24.utilities.dto.shallows;

public class AccountShallowDto {

    private String email = "";

    private String tipoAccount = "";

    public AccountShallowDto(String email, String tipoAccount) {
        this.email = email;
        this.tipoAccount = tipoAccount;
    }

    public AccountShallowDto() {
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoAccount() {
        return this.tipoAccount;
    }

    public void setTipoAccount(String tipoAccount) {
        this.tipoAccount = tipoAccount;
    }
}
