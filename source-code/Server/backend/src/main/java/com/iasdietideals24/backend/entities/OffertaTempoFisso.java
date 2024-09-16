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
@DiscriminatorValue("Tempo fisso")
public class OffertaTempoFisso extends OffertaDiCompratore {
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fk_astatempofisso_for_offertatempofisso")
    @NonNull
    private AstaTempoFisso astaRiferimento;

    // AllArgsConstructor
    public OffertaTempoFisso(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato, @NonNull AstaTempoFisso astaRiferimento) {
        super(dataInvio, oraInvio, valore, compratoreCollegato);

        this.astaRiferimento = astaRiferimento;
        astaRiferimento.addOffertaRicevuta(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffertaTempoFisso)) return false;
        if (!super.equals(o)) return false;
        OffertaTempoFisso offerta = (OffertaTempoFisso) o;
        return Objects.equals(this.astaRiferimento, offerta.getAstaRiferimento());
    }
}
