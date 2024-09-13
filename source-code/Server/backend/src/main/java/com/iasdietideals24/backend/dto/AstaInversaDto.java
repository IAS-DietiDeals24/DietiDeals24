package com.iasdietideals24.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

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

    private HashSet<byte[]> immagini;

    private HashSet<NotificaDto> notificheAssociate;

    private CompratoreDto proprietario;

    private BigDecimal sogliaIniziale;

    private HashSet<OffertaInversa> offerteRicevute;
}
