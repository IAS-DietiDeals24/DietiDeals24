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
@DiscriminatorValue("Silenziosa")
public class OffertaSilenziosa extends OffertaDiCompratore {
    private Boolean isAccettata = null;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_astasilenziosa_idasta")
    @NonNull
    private AstaSilenziosa astaRiferimento;

    // AllArgsConstructor
    public OffertaSilenziosa(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato, Boolean isAccettata, @NonNull AstaSilenziosa astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.isAccettata = isAccettata;

        this.astaRiferimento = astaRiferimento;
        astaRiferimento.addOffertaRicevuta(this);
    }

    @Override
    public String toString() {
        return "OffertaSilenziosa(isAccettata=" + this.getIsAccettata() + ", astaRiferimento=" + this.getAstaRiferimento().getIdAsta() + ") is a " + super.toString();
    }
}
