package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OffertaDiCompratore extends Offerta {
    @NonNull private Compratore compratoreCollegato;

    //AllArgsConstructor
    public OffertaDiCompratore (LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, Compratore compratoreCollegato) {
        super(dataInvio, oraInvio, valore);

        this.setCompratoreCollegato(compratoreCollegato);
    }
}
