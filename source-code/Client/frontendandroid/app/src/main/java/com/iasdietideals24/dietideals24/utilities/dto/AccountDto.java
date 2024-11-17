package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Account;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto;

import java.util.HashSet;
import java.util.Set;

public abstract class AccountDto {

    protected String email = "";

    protected String password = "";

    protected TokensAccountDto tokens = new TokensAccountDto();

    protected ProfiloShallowDto profiloShallow = new ProfiloShallowDto();

    protected Set<NotificaShallowDto> notificheInviateShallow = new HashSet<>();

    protected Set<NotificaShallowDto> notificheRicevuteShallow = new HashSet<>();

    protected Set<AstaShallowDto> astePosseduteShallow = new HashSet<>();

    protected Set<OffertaShallowDto> offerteCollegateShallow = new HashSet<>();

    public AccountDto() {
    }

    public abstract Account toAccount();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TokensAccountDto getTokens() {
        return this.tokens;
    }

    public ProfiloShallowDto getProfiloShallow() {
        return this.profiloShallow;
    }

    public Set<NotificaShallowDto> getNotificheInviateShallow() {
        return this.notificheInviateShallow;
    }

    public Set<NotificaShallowDto> getNotificheRicevuteShallow() {
        return this.notificheRicevuteShallow;
    }

    public Set<AstaShallowDto> getAstePosseduteShallow() {
        return this.astePosseduteShallow;
    }

    public Set<OffertaShallowDto> getOfferteCollegateShallow() {
        return this.offerteCollegateShallow;
    }
}
