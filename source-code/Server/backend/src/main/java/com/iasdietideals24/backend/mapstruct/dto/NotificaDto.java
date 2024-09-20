package com.iasdietideals24.backend.mapstruct.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class NotificaDto {

    private Long idNotifica;

    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private String messaggio;

    private String emailMittente;

    private Set<String> emailDestinatari;

    private Long idAstaAssociata;
}
