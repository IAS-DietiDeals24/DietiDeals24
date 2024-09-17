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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoOfferta")
public abstract class OffertaDiVenditore extends Offerta {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_venditore_email")
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
