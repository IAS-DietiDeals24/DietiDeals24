package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@DiscriminatorValue("Silenziosa")
public class OffertaSilenziosa extends OffertaDiCompratore {
    private Boolean isAccettata = null;

    @ManyToOne
    @JoinColumn(name = "fk_astasilenziosa")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffertaSilenziosa)) return false;
        if (!super.equals(o)) return false;
        OffertaSilenziosa offerta = (OffertaSilenziosa) o;
        return Objects.equals(this.isAccettata, offerta.getIsAccettata()) && Objects.equals(this.astaRiferimento, offerta.getAstaRiferimento());
    }
}
