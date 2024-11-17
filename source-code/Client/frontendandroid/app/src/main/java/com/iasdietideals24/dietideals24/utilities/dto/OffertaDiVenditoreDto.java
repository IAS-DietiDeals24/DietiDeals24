package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class OffertaDiVenditoreDto extends OffertaDto {

    protected AccountShallowDto venditoreCollegatoShallow = new AccountShallowDto();

    public OffertaDiVenditoreDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto venditoreCollegatoShallow, AstaShallowDto astaRiferimentoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore, astaRiferimentoShallow);
        this.venditoreCollegatoShallow = venditoreCollegatoShallow;
    }

    public OffertaDiVenditoreDto() {
    }

    public AccountShallowDto getVenditoreCollegatoShallow() {
        return this.venditoreCollegatoShallow;
    }
}
