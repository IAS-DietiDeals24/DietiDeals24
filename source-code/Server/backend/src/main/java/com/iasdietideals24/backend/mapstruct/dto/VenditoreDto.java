package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class VenditoreDto extends AccountDto {

    private Set<AstaDiVenditoreDto> astePossedute;

    private Set<OffertaDiVenditoreDto> offerteCollegate;
}