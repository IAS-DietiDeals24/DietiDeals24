package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AstaDiVenditoreDto extends AstaDto {

    private VenditoreDto proprietario;
}
