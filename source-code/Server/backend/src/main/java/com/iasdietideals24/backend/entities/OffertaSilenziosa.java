package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.entities.utilities.StatoOffertaSilenziosa;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "offerta_silenziosa")
public class OffertaSilenziosa extends OffertaDiCompratore {
    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "stato", nullable = false)
    private StatoOffertaSilenziosa stato = StatoOffertaSilenziosa.PENDING;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "asta_silenziosa_id_asta", nullable = false)
    @NonNull
    private AstaSilenziosa astaRiferimento;

    // AllArgsConstructor
    public OffertaSilenziosa(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato, StatoOffertaSilenziosa stato, @NonNull AstaSilenziosa astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.stato = stato;

        this.astaRiferimento = astaRiferimento;
        astaRiferimento.addOffertaRicevuta(this);
    }

    @Override
    public String toString() {
        return "OffertaSilenziosa(stato=" + this.getStato() + ", astaRiferimento=" + this.getAstaRiferimento().getIdAsta() + ") is a " + super.toString();
    }
}
