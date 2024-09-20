package com.iasdietideals24.backend.mapstruct.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AstaInversaDto {

    private Long idAsta;

    private String categoria;

    private String nome;

    private String descrizione;

    private LocalDate dataScadenza;

    private LocalTime oraScadenza;

    private byte[] immagine;

    private Set<Long> idNotificheAssociate;

    private String emailProprietario;

    private BigDecimal sogliaIniziale;

    private Set<Long> idOfferteRicevute;
}
