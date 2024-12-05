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
@Entity(name = "asta_inversa")
@Check(constraints = "soglia_iniziale >= 0")
public class AstaInversa extends AstaDiCompratore {
    @NonNull
    @Column(name = "soglia_iniziale", nullable = false, scale = 2, precision = 10)
    private BigDecimal sogliaIniziale;

    @OneToMany(mappedBy = "astaRiferimento", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<OffertaInversa> offerteRicevute = new LinkedHashSet<>();

    // AllArgsConstructor
    public AstaInversa(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Compratore proprietario, @NonNull BigDecimal sogliaIniziale, StatoAsta statoAsta) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario, statoAsta);

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
