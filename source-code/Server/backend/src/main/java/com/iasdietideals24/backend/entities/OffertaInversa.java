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
@DiscriminatorValue("Inversa")
public class OffertaInversa extends OffertaDiVenditore {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_astainversa_idasta")
    @NonNull
    private AstaInversa astaRiferimento;

    // AllArgsConstructor
    public OffertaInversa(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Venditore venditoreCollegato, @NonNull AstaInversa astaRiferimento) {
        super(dataInvio, oraInvio, valore, venditoreCollegato);

        this.astaRiferimento = astaRiferimento;
        astaRiferimento.addOffertaRicevuta(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffertaInversa)) return false;
        if (!super.equals(o)) return false;
        OffertaInversa offerta = (OffertaInversa) o;
        return Objects.equals(this.astaRiferimento, offerta.getAstaRiferimento());
    }

    @Override
    public String toString() {
        return "OffertaInversa(astaRiferimento=" + this.getAstaRiferimento().getIdAsta() + ") is a " + super.toString();
    }
}
