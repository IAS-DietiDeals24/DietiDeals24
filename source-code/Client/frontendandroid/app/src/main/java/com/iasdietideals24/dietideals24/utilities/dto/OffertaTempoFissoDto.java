package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaTempoFissoDto {

    private Long idOfferta;

    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private BigDecimal valore;

    private AccountShallowDto compratoreCollegatoShallow;

    private AstaShallowDto astaRiferimentoShallow;
}
