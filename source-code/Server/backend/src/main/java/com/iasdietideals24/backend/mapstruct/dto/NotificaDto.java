package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
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

    private AccountShallowDto mittenteShallow;

    private Set<AccountShallowDto> destinatariShallow;

    private AstaShallowDto astaAssociataShallow;
}
