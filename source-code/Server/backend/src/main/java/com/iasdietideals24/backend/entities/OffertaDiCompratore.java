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
public abstract class OffertaDiCompratore extends Offerta {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_compratore_email")
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

    public String toString() {
        return "OffertaDiCompratore(compratoreCollegato=" + this.getCompratoreCollegato().getEmail() + ") is a " + super.toString();
    }
}
