package com.iasdietideals24.backend.mapstruct.dto;

import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class OffertaSilenziosaDto {

    private Long idOfferta;

    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private BigDecimal valore;

    private AccountShallowDto compratoreCollegatoShallow;

    private Boolean isAccettata = null;

    private AstaShallowDto astaRiferimentoShallow;
}
