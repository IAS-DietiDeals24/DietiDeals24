package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompratoreDto {

    private String email;

    private String password;

    private TokensAccountDto tokens;

    private ProfiloShallowDto profiloShallow;

    private Set<NotificaShallowDto> notificheInviateShallow;

    private Set<NotificaShallowDto> notificheRicevuteShallow;

    private Set<AstaShallowDto> astePosseduteShallow;

    private Set<OffertaShallowDto> offerteCollegateShallow;
}
