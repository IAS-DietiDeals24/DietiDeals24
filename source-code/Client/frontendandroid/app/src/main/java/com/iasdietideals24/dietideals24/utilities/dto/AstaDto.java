package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AstaDto {
    protected Long idAsta;
    protected String categoria;
    protected String nome;
    protected String descrizione;
    protected LocalDate dataScadenza;
    protected LocalTime oraScadenza;
    protected byte[] immagine;
    protected Set<NotificaShallowDto> notificheAssociateShallow;

    public AstaDto(Long idAsta, String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow) {
        this.idAsta = idAsta;
        this.categoria = categoria;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.oraScadenza = oraScadenza;
        this.immagine = immagine;
        this.notificheAssociateShallow = notificheAssociateShallow;
    }
}
