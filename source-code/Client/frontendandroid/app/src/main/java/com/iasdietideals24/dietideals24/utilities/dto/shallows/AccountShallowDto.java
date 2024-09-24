package com.iasdietideals24.dietideals24.utilities.dto.shallows;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountShallowDto {

    private String email = "";

    private String tipoAccount = "";

    public AccountShallowDto(String email, String tipoAccount) {
        this.email = email;
        this.tipoAccount = tipoAccount;
    }
}
