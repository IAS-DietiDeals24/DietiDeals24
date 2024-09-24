package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount;
import com.iasdietideals24.dietideals24.utilities.classes.data.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompratoreDto extends AccountDto {

    public Account toAccount() {
        return new Account(tokens.getIdFacebook(), email, password, TipoAccount.COMPRATORE);
    }
}
