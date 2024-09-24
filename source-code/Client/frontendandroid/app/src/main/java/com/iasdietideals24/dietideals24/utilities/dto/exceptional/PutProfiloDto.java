package com.iasdietideals24.dietideals24.utilities.dto.exceptional;

import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount;
import com.iasdietideals24.dietideals24.utilities.classes.data.Account;
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

    private String nomeUtente = "";

    private byte[] profilePicture = new byte[0];

    private AnagraficaProfiloDto anagrafica = new AnagraficaProfiloDto();

    private LinksProfiloDto links = new LinksProfiloDto();

    private String email = "";

    private String password = "";

    private TokensAccountDto tokens = new TokensAccountDto();

    private String tipoAccount = "";

    public PutProfiloDto(String nomeUtente, byte[] profilePicture, AnagraficaProfiloDto anagrafica, LinksProfiloDto links, String email, String password, TokensAccountDto tokens, String tipoAccount) {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.anagrafica = anagrafica;
        this.links = links;
        this.email = email;
        this.password = password;
        this.tokens = tokens;
        this.tipoAccount = tipoAccount;
    }

    public Account toAccount() {
        return new Account(tokens.getIdFacebook(), email, password, TipoAccount.valueOf(tipoAccount));
    }

}
