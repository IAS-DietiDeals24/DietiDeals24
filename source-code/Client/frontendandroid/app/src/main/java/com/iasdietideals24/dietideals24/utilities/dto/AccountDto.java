package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto {

    protected String email = "";

    protected String password = "";

    protected TokensAccountDto tokens = new TokensAccountDto();

    protected ProfiloShallowDto profiloShallow = new ProfiloShallowDto();

    protected Set<NotificaShallowDto> notificheInviateShallow = new HashSet<>();

    protected Set<NotificaShallowDto> notificheRicevuteShallow = new HashSet<>();

    public String getEmail() {
        return email;
    }
}
