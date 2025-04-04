package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaSilenziosaDto extends OffertaDiCompratoreDto {

    private String stato;

    private AstaShallowDto astaRiferimentoShallow;
}
