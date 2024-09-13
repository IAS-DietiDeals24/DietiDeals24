package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class OffertaDiCompratore extends Offerta {
    @NonNull
    private Compratore compratoreCollegato;

    // AllArgsConstructor
    public OffertaDiCompratore(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato) {
        super(dataInvio, oraInvio, valore);

        this.setCompratoreCollegato(compratoreCollegato);
        compratoreCollegato.addOffertaCollegata(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffertaDiCompratore)) return false;
        if (!super.equals(o)) return false;
        OffertaDiCompratore offerta = (OffertaDiCompratore) o;
        return Objects.equals(this.compratoreCollegato, offerta.getCompratoreCollegato());
    }
}
