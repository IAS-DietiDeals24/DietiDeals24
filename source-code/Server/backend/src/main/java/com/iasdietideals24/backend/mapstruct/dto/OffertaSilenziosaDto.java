package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaSilenziosaDto extends OffertaDiCompratoreDto {

    private Boolean isAccettata = null;

    private AstaSilenziosaDto astaRiferimento;
}
