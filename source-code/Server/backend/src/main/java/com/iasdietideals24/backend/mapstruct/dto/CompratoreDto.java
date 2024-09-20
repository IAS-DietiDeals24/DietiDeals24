package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompratoreDto {

    private String email;

    private String password;

    private String nomeUtenteProfilo;

    private Set<Long> idNotificheInviate;

    private Set<Long> idNotificheRicevute;

    private Set<Long> idAstePossedute;

    private Set<Long> idOfferteCollegate;
}
