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
@DiscriminatorValue("Tempo fisso")
public class OffertaTempoFisso extends OffertaDiCompratore {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_astatempofisso_idasta")
    @NonNull
    private AstaTempoFisso astaRiferimento;

    // AllArgsConstructor
    public OffertaTempoFisso(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato, @NonNull AstaTempoFisso astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.astaRiferimento = astaRiferimento;
        astaRiferimento.addOffertaRicevuta(this);
    }

    @Override
    public String toString() {
        return "OffertaTempoFisso(astaRiferimento=" + this.getAstaRiferimento().getIdAsta() + ") is a " + super.toString();
    }
}
