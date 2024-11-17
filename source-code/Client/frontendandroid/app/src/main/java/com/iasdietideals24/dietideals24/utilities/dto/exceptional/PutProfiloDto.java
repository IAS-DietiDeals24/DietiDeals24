package com.iasdietideals24.dietideals24.utilities.dto.exceptional;

import com.iasdietideals24.dietideals24.utilities.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.LinksProfiloDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto;

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

    public PutProfiloDto() {
    }

    public String getNomeUtente() {
        return this.nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoAccount() {
        return this.tipoAccount;
    }

    public void setTipoAccount(String tipoAccount) {
        this.tipoAccount = tipoAccount;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public AnagraficaProfiloDto getAnagrafica() {
        return anagrafica;
    }

    public LinksProfiloDto getLinks() {
        return links;
    }

    public TokensAccountDto getTokens() {
        return tokens;
    }
}
