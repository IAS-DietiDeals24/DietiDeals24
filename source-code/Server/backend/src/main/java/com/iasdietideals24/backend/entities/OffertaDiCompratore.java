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
@Entity(name = "offerta_di_compratore")
public abstract class OffertaDiCompratore extends Offerta {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "compratore_email", nullable = false)
    @NonNull
    private Compratore compratoreCollegato;

    // AllArgsConstructor
    protected OffertaDiCompratore(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore, @NonNull Compratore compratoreCollegato) {
        super(dataInvio, oraInvio, valore);

        this.setCompratoreCollegato(compratoreCollegato);
        compratoreCollegato.addOffertaCollegata(this);
    }

    @Override
    public String toString() {
        return "OffertaDiCompratore(compratoreCollegato=" + this.getCompratoreCollegato().getEmail() + ") is a " + super.toString();
    }
}
