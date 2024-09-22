package com.iasdietideals24.dietideals24.utilities.dto.exceptional;


import com.iasdietideals24.dietideals24.utilities.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.LinksProfiloDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PutProfiloDto {

    private String nomeUtente;

    private byte[] profilePicture;

    private AnagraficaProfiloDto anagrafica;

    private LinksProfiloDto links;

    private String email;

    private String password;

    private TokensAccountDto tokens;

    private String tipoAccount;
}
