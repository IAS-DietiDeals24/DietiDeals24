package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AstaDto {

    private Long idAsta;

    private String stato;

    private String nome;

    private String descrizione;

    private LocalDate dataScadenza;

    private LocalTime oraScadenza;

    private byte[] immagine;

    private CategoriaAstaShallowDto categoriaShallow;

    private Set<NotificaShallowDto> notificheAssociateShallow;
}
