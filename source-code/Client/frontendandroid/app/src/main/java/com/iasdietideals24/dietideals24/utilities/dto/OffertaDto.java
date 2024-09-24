package com.iasdietideals24.dietideals24.utilities.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OffertaDto {

    protected Long idOfferta = 0L;

    protected LocalDate dataInvio = LocalDate.now();

    protected LocalTime oraInvio = LocalTime.now();

    protected BigDecimal valore = BigDecimal.ZERO;

    public OffertaDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore) {
        this.idOfferta = idOfferta;
        this.dataInvio = dataInvio;
        this.oraInvio = oraInvio;
        this.valore = valore;
    }
}
