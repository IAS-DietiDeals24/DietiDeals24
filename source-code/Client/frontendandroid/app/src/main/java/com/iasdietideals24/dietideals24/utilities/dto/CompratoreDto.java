package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Account;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount;

public class CompratoreDto extends AccountDto {

    public CompratoreDto() {
    }

    public Account toAccount() {
        return new Account(tokens.getIdFacebook(), email, password, TipoAccount.COMPRATORE);
    }
}
