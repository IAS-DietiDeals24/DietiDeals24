package com.iasdietideals24.backend.mapstruct.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AstaInversaDto extends AstaDiCompratoreDto {

    private BigDecimal sogliaIniziale;

    private Set<OffertaInversaDto> offerteRicevute;
}
