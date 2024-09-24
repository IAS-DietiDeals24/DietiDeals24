package com.iasdietideals24.dietideals24.utilities.dto.shallows;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AstaShallowDto {

    private Long idAsta = 0L;

    private String tipoAstaPerAccount = "";

    private String tipoAstaSpecifica = "";

    public AstaShallowDto(Long idAsta, String tipoAstaPerAccount, String tipoAstaSpecifica) {
        this.idAsta = idAsta;
        this.tipoAstaPerAccount = tipoAstaPerAccount;
        this.tipoAstaSpecifica = tipoAstaSpecifica;
    }
}
