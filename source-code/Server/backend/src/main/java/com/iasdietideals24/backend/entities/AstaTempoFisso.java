package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
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
@DiscriminatorValue("Tempo fisso")
public class AstaTempoFisso extends AstaDiVenditore {
    @NonNull
    private BigDecimal sogliaMinima;

    @OneToMany(mappedBy = "astaRiferimento", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<OffertaTempoFisso> offerteRicevute;

    // AllArgsConstructor
    public AstaTempoFisso(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario, @NonNull BigDecimal sogliaMinima) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, proprietario);

        this.setSogliaMinima(sogliaMinima);
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaTempoFisso offertaDaAggiungere) {        
        if (this.offerteRicevute == null)
            this.offerteRicevute = new LinkedHashSet<OffertaTempoFisso>();
        
        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaTempoFisso offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);

        if (this.offerteRicevute.isEmpty())
            this.offerteRicevute = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaTempoFisso)) return false;
        if (!super.equals(o)) return false;
        AstaTempoFisso asta = (AstaTempoFisso) o;
        return Objects.equals(this.sogliaMinima, asta.getSogliaMinima()) && Objects.equals(this.offerteRicevute, asta.getOfferteRicevute());
    }
}
