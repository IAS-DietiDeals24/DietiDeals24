package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.LinksProfiloDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProfiloDto {

    private String nomeUtente;

    private byte[] profilePicture;

    private AnagraficaProfiloDto anagrafica;

    private LinksProfiloDto links;

    private Set<AccountShallowDto> accountsShallow;
}
