package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AstaSilenziosaDto {

    private Long idAsta;

    private String categoria;

    private String nome;

    private String descrizione;

    private LocalDate dataScadenza;

    private LocalTime oraScadenza;

    private byte[] immagine;

    private Set<NotificaShallowDto> notificheAssociateShallow;

    private AccountShallowDto proprietarioShallow;

    private Set<OffertaShallowDto> offerteRicevuteShallow;
}
