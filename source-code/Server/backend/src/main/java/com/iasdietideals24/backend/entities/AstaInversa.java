package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@ToString
public class AstaInversa extends AstaDiCompratore {
    @NonNull
    private BigDecimal sogliaIniziale;

    @Setter(AccessLevel.NONE)
    private Set<OffertaInversa> offerteRicevute;

    // AllArgsConstructor
    public AstaInversa(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Compratore proprietario, @NonNull BigDecimal sogliaIniziale) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario);

        this.sogliaIniziale = sogliaIniziale;
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaInversa offertaDaAggiungere) {        
        if (this.offerteRicevute == null)
            this.offerteRicevute = new LinkedHashSet<OffertaInversa>();

        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaInversa offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);

        if (this.offerteRicevute.isEmpty())
            this.offerteRicevute = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaInversa)) return false;
        if (!super.equals(o)) return false;
        AstaInversa asta = (AstaInversa) o;
        return Objects.equals(this.sogliaIniziale, asta.getSogliaIniziale()) && Objects.equals(this.offerteRicevute, asta.getOfferteRicevute());
    }
}
