package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class OffertaDiVenditore extends Offerta {
    @NonNull
    private Venditore venditoreCollegato;

    // AllArgsConstructor
    public OffertaDiVenditore(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Venditore venditoreCollegato) {
        super(dataInvio, oraInvio, valore);

        this.setVenditoreCollegato(venditoreCollegato);
        venditoreCollegato.addOffertaCollegata(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffertaDiVenditore)) return false;
        if (!super.equals(o)) return false;
        OffertaDiVenditore offerta = (OffertaDiVenditore) o;
        return Objects.equals(this.venditoreCollegato, offerta.getVenditoreCollegato());
    }
}
