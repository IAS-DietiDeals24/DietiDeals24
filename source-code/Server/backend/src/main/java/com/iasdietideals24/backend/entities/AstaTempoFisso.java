package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "asta_tempo_fisso")
@Check(constraints = "soglia_minima >= 0")
public class AstaTempoFisso extends AstaDiVenditore {
    @NonNull
    @Column(name = "soglia_minima", nullable = false, scale = 2, precision = 10)
    private BigDecimal sogliaMinima;

    @OneToMany(mappedBy = "astaRiferimento", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<OffertaTempoFisso> offerteRicevute = new LinkedHashSet<>();

    // AllArgsConstructor
    public AstaTempoFisso(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario, @NonNull BigDecimal sogliaMinima, StatoAsta statoAsta) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario, statoAsta);

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
