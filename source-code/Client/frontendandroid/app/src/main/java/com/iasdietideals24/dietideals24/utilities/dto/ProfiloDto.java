package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.AnteprimaProfilo;
import com.iasdietideals24.dietideals24.utilities.data.Profilo;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.LinksProfiloDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount;

import java.util.HashSet;
import java.util.Set;

public class ProfiloDto {
    private String nomeUtente = "";

    private byte[] profilePicture = new byte[0];

    private AnagraficaProfiloDto anagrafica = new AnagraficaProfiloDto();

    private LinksProfiloDto links = new LinksProfiloDto();

    private Set<AccountShallowDto> accountsShallow = new HashSet<>();

    public ProfiloDto(String nomeUtente, byte[] profilePicture, AnagraficaProfiloDto anagrafica, LinksProfiloDto links, Set<AccountShallowDto> accountsShallow) {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.anagrafica = anagrafica;
        this.links = links;
        this.accountsShallow = accountsShallow;
    }

    public ProfiloDto() {
    }

    public Profilo toProfilo() {
        return new Profilo(
                accountsShallow.stream().findFirst().orElse(new AccountShallowDto()).getEmail(),
                accountsShallow.stream().findFirst().orElse(new AccountShallowDto()).getTipoAccount(),
                nomeUtente,
                profilePicture,
                anagrafica.getNome(),
                anagrafica.getCognome(),
                accountsShallow.stream().findFirst().orElse(new AccountShallowDto()).getEmail(),
                anagrafica.getDataNascita(),
                anagrafica.getGenere(),
                anagrafica.getAreaGeografica(),
                anagrafica.getBiografia(),
                links.getLinkInstagram(),
                links.getLinkFacebook(),
                links.getLinkGitHub(),
                links.getLinkX(),
                links.getLinkPersonale()
        );
    }

    public AnteprimaProfilo toAnteprimaProfilo() {
        return new AnteprimaProfilo(
                nomeUtente,
                TipoAccount.valueOf(accountsShallow.stream().findFirst().orElse(new AccountShallowDto()).getTipoAccount()),
                profilePicture
        );
    }

    public String getNomeUtente() {
        return this.nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }
}
