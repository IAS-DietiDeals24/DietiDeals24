package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaTempoFissoDto extends OffertaDiCompratoreDto {

    private AstaTempoFissoDto astaRiferimento;
}
