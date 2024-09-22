package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VenditoreDto {

    private String email;

    private String password;

    private TokensAccountDto tokens;

    private ProfiloShallowDto profiloShallow;

    private Set<NotificaShallowDto> notificheInviateShallow;

    private Set<NotificaShallowDto> notificheRicevuteShallow;

    private Set<AstaShallowDto> astePosseduteShallow;

    private Set<OffertaShallowDto> offerteCollegateShallow;
}