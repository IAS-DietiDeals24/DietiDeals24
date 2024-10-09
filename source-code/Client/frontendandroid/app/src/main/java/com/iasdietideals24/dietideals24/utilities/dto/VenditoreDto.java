package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Account;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount;

public class VenditoreDto extends AccountDto {

    public VenditoreDto() {
    }

    public Account toAccount() {
        return new Account(tokens.getIdFacebook(), email, password, TipoAccount.VENDITORE);
    }
}