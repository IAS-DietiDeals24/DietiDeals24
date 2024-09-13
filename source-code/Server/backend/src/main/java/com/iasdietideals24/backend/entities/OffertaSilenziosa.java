package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@ToString
public class OffertaSilenziosa extends OffertaDiCompratore {
    private Boolean isAccettata = null;

    @NonNull
    private AstaSilenziosa astaRiferimento;

    // AllArgsConstructor
    public OffertaSilenziosa(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato, Boolean isAccettata, @NonNull AstaSilenziosa astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.isAccettata = isAccettata;

        this.astaRiferimento = astaRiferimento;
        astaRiferimento.addOffertaRicevuta(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffertaSilenziosa)) return false;
        if (!super.equals(o)) return false;
        OffertaSilenziosa offerta = (OffertaSilenziosa) o;
        return Objects.equals(this.isAccettata, offerta.getIsAccettata()) && Objects.equals(this.astaRiferimento, offerta.getAstaRiferimento());
    }
}
