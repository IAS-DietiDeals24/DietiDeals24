package com.iasdietideals24.backend.mapstruct.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AstaSilenziosaDto extends AstaDiVenditoreDto {

    private Set<OffertaSilenziosaDto> offerteRicevute;
}
