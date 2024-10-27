package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
public abstract class OffertaDiVenditore extends Offerta {
    @ManyToOne
    @JoinColumn(name = "venditore_email", nullable = false)
    @NonNull
    private Venditore venditoreCollegato;

    // AllArgsConstructor
    protected OffertaDiVenditore(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Venditore venditoreCollegato) {
        super(dataInvio, oraInvio, valore);

        this.setVenditoreCollegato(venditoreCollegato);
        venditoreCollegato.addOffertaCollegata(this);
    }

    @Override
    public String toString() {
        return "OffertaDiVenditore(venditoreCollegato=" + this.getVenditoreCollegato().getEmail() + ") is a " + super.toString();
    }
}
