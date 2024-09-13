package com.iasdietideals24.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.iasdietideals24.backend.entities.OffertaInversa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AstaInversaDto {

    private Long idAsta;

    private String categoria;

    private String nome;

    private String descrizione;

    private LocalDate dataScadenza;

    private LocalTime oraScadenza;

    private Set<byte[]> immagini;

    private Set<NotificaDto> notificheAssociate;

    private CompratoreDto proprietario;

    private BigDecimal sogliaIniziale;

    private Set<OffertaInversa> offerteRicevute;
}
