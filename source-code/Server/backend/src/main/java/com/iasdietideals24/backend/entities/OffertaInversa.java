package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class OffertaInversa extends OffertaDiVenditore {
    @NonNull private AstaInversa astaRiferimento;

    public OffertaInversa (LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, Venditore venditoreCollegato, AstaInversa astaRiferimento) {
        super(dataInvio, oraInvio, valore, venditoreCollegato);

        this.setAstaRiferimento(astaRiferimento);
    }
}
