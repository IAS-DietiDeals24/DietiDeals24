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
    public String toString() {
        return "OffertaInversa(astaRiferimento=" + this.getAstaRiferimento().getIdAsta() + ") is a " + super.toString();
    }
}
