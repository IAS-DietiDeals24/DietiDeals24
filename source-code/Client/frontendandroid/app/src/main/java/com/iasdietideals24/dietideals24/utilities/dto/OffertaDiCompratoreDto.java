package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class OffertaDiCompratoreDto extends OffertaDto {

    protected AccountShallowDto compratoreCollegatoShallow = new AccountShallowDto();

    public OffertaDiCompratoreDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto compratoreCollegatoShallow, AstaShallowDto astaRiferimentoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore, astaRiferimentoShallow);
        this.compratoreCollegatoShallow = compratoreCollegatoShallow;
    }

    public OffertaDiCompratoreDto() {
    }

    public AccountShallowDto getCompratoreCollegatoShallow() {
        return this.compratoreCollegatoShallow;
    }
}
