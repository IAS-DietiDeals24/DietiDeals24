package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoOfferta")
public abstract class OffertaDiVenditore extends Offerta {
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fk_venditore_for_offertadivenditore")
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
