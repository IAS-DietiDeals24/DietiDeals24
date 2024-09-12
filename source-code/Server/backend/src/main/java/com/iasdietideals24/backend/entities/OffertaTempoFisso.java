package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class OffertaTempoFisso extends OffertaDiCompratore {
    @NonNull private AstaTempoFisso astaRiferimento;

    public OffertaTempoFisso (LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, Compratore compratoreCollegato, AstaTempoFisso astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.setAstaRiferimento(astaRiferimento);
    }
}
