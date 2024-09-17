package com.iasdietideals24.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("Inversa")
public class AstaInversa extends AstaDiCompratore {
    @NonNull
    private BigDecimal sogliaIniziale;

    @OneToMany(mappedBy = "astaRiferimento", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<OffertaInversa> offerteRicevute = new LinkedHashSet<OffertaInversa>();

    // AllArgsConstructor
    public AstaInversa(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Compratore proprietario, @NonNull BigDecimal sogliaIniziale) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario);

        this.sogliaIniziale = sogliaIniziale;
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaInversa offertaDaAggiungere) {
        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaInversa offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaInversa)) return false;
        if (!super.equals(o)) return false;
        AstaInversa asta = (AstaInversa) o;
        return Objects.equals(this.sogliaIniziale, asta.getSogliaIniziale()) && Objects.equals(this.offerteRicevute, asta.getOfferteRicevute());
    }

    @Override
    public String toString() {
        Iterator<OffertaInversa> itrOffertaRicevuta = this.getOfferteRicevute().iterator();
        StringBuilder listIdOfferteRicevute = new StringBuilder();
        listIdOfferteRicevute.append("[");
        while (itrOffertaRicevuta.hasNext()) {
            listIdOfferteRicevute.append(itrOffertaRicevuta.next().getIdOfferta()).append(", ");
        }
        listIdOfferteRicevute.append("]");

        return "AstaInversa(sogliaIniziale=" + this.getSogliaIniziale() + ", offerteRicevute=" + listIdOfferteRicevute + ") is a " + super.toString();
    }
}
