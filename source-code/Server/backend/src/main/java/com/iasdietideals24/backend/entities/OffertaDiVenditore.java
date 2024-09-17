package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoOfferta")
public abstract class OffertaDiVenditore extends Offerta {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_venditore_email")
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

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), venditoreCollegato);
    }

    @Override
    public String toString() {
        return "OffertaDiVenditore(venditoreCollegato=" + this.getVenditoreCollegato().getEmail() + ") is a " + super.toString();
    }
}
