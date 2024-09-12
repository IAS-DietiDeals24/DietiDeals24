package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OffertaDiVenditore extends Offerta {
    @NonNull private Venditore venditoreCollegato;

    //AllArgsConstructor
    public OffertaDiVenditore (LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, Venditore venditoreCollegato) {
        super(dataInvio, oraInvio, valore);

        this.setVenditoreCollegato(venditoreCollegato);
    }
}
