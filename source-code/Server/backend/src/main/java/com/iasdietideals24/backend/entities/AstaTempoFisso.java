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
@DiscriminatorValue("Tempo fisso")
public class AstaTempoFisso extends AstaDiVenditore {
    @NonNull
    private BigDecimal sogliaMinima;

    @OneToMany(mappedBy = "astaRiferimento", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<OffertaTempoFisso> offerteRicevute = new LinkedHashSet<OffertaTempoFisso>();

    // AllArgsConstructor
    public AstaTempoFisso(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario, @NonNull BigDecimal sogliaMinima) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario);

        this.setSogliaMinima(sogliaMinima);
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaTempoFisso offertaDaAggiungere) {
        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaTempoFisso offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaTempoFisso)) return false;
        if (!super.equals(o)) return false;
        AstaTempoFisso asta = (AstaTempoFisso) o;
        return Objects.equals(this.sogliaMinima, asta.getSogliaMinima()) && Objects.equals(this.offerteRicevute, asta.getOfferteRicevute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sogliaMinima, offerteRicevute);
    }

    @Override
    public String toString() {
        Iterator<OffertaTempoFisso> itrOffertaRicevuta = this.getOfferteRicevute().iterator();
        StringBuilder listIdOfferteRicevute = new StringBuilder();
        listIdOfferteRicevute.append("[");
        while (itrOffertaRicevuta.hasNext()) {
            listIdOfferteRicevute.append(itrOffertaRicevuta.next().getIdOfferta()).append(", ");
        }
        listIdOfferteRicevute.append("]");

        return "AstaTempoFisso(sogliaMinima=" + this.getSogliaMinima() + ", offerteRicevute=" + listIdOfferteRicevute + ") is a " + super.toString();
    }
}
