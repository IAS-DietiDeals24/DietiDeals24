package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Account;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount;

public class VenditoreDto extends AccountDto {

    public VenditoreDto(String email, String password, TokensAccountDto tokens, ProfiloShallowDto profiloShallow) {
        super();

        this.email = email;
        this.password = password;
        this.tokens = tokens;
        this.profiloShallow = profiloShallow;
    }

    public VenditoreDto() {
    }

    public Account toAccount() {
        return new Account(tokens.getIdFacebook(), email, password, TipoAccount.VENDITORE);
    }
}