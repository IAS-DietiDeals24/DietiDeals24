package com.iasdietideals24.backend.mapstruct.dto.shallows;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AstaShallowDto {

    private Long idAsta;

    private String tipoAstaPerAccount;

    private String tipoAstaSpecifica;
}