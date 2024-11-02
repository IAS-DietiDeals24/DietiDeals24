package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class OffertaDiVenditoreDto extends OffertaDto {

    protected AccountShallowDto venditoreCollegatoShallow = new AccountShallowDto();

    public OffertaDiVenditoreDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto venditoreCollegatoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore);
        this.venditoreCollegatoShallow = venditoreCollegatoShallow;
    }

    public OffertaDiVenditoreDto() {
    }

    public AccountShallowDto getVenditoreCollegatoShallow() {
        return this.venditoreCollegatoShallow;
    }
}
