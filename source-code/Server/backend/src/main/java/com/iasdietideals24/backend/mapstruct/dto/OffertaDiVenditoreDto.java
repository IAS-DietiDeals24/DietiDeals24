package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaDiVenditoreDto extends OffertaDto {

    private AccountShallowDto venditoreCollegatoShallow;
}
