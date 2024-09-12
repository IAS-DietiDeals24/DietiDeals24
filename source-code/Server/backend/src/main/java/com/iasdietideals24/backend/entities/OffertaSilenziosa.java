package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class OffertaSilenziosa extends OffertaDiCompratore {
    private Boolean isAccettata = null;

    @NonNull private AstaSilenziosa astaRiferimento;

    public OffertaSilenziosa (LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, Compratore compratoreCollegato, Boolean isAccettata, AstaSilenziosa astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.setIsAccettata(isAccettata);
        this.setAstaRiferimento(astaRiferimento);
    }
}
