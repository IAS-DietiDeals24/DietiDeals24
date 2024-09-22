package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
