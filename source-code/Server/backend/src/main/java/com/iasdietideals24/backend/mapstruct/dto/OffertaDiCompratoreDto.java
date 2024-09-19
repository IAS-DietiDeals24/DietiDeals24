package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class OffertaDiCompratoreDto extends OffertaDto {

    private CompratoreDto compratoreCollegato;
}
