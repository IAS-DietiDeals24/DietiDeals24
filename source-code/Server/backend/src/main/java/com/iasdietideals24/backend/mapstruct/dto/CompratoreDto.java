package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompratoreDto extends AccountDto {

    private Set<AstaDiCompratoreDto> astePossedute;

    private Set<OffertaDiCompratoreDto> offerteCollegate;
}
