package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount;
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaProfilo;
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.dietideals24.utilities.dto.utilities.LinksProfiloDto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfiloDto {
    private String nomeUtente;
    private byte[] profilePicture;
    private AnagraficaProfiloDto anagrafica;
    private LinksProfiloDto links;
    private Set<AccountShallowDto> accountsShallow;

    public ProfiloDto(String nomeUtente, byte[] profilePicture, AnagraficaProfiloDto anagrafica, LinksProfiloDto links, Set<AccountShallowDto> accountsShallow) {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.anagrafica = anagrafica;
        this.links = links;
        this.accountsShallow = accountsShallow;
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
}
