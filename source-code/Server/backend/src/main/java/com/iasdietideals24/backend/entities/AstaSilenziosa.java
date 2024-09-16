package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@DiscriminatorValue("Silenziosa")
public class AstaSilenziosa extends AstaDiVenditore {
    @OneToMany(mappedBy = "astaRiferimento", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<OffertaSilenziosa> offerteRicevute = new LinkedHashSet<OffertaSilenziosa>();

    // AllArgsConstructor
    public AstaSilenziosa(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario);
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaSilenziosa offertaDaAggiungere) {        
        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaSilenziosa offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaSilenziosa)) return false;
        if (!super.equals(o)) return false;
        AstaSilenziosa asta = (AstaSilenziosa) o;
        return Objects.equals(this.offerteRicevute, asta.getOfferteRicevute());
    }
}
