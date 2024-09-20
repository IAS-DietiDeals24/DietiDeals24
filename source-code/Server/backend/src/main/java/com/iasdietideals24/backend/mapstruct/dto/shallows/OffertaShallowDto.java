package com.iasdietideals24.backend.mapstruct.dto.shallows;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaShallowDto {

    private Long idOfferta;

    private String tipoOffertaPerAccount;

    private String tipoOffertaSpecifica;
}
