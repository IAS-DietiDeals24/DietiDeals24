package com.iasdietideals24.backend.mapstruct.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public abstract class OffertaDiVenditoreDto extends OffertaDto {

    private VenditoreDto venditoreCollegato;
}
