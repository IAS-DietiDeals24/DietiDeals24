package com.iasdietideals24.backend.mapstruct.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email")
public abstract class AccountDto {

    private String email;

    private String password;

    private ProfiloDto profilo;

    private Set<NotificaDto> notificheInviate;

    private Set<NotificaDto> notificheRicevute;
}
