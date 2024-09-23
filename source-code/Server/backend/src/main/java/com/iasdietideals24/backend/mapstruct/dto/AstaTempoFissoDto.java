package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AstaTempoFissoDto extends AstaDiVenditoreDto {

    private BigDecimal sogliaMinima;

    private Set<OffertaShallowDto> offerteRicevuteShallow;
}
