package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class AstaTempoFisso extends AstaDiVenditore {
    @NonNull private BigDecimal sogliaMinima;

    private Set<OffertaTempoFisso> offerteRicevute;

    // AllArgsConstructor
    public AstaTempoFisso (String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, Set<byte[]> immagini, Set<Notifica> notificheAssociate, Venditore proprietario, BigDecimal sogliaMinima, Set<OffertaTempoFisso> offerteRicevute) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagini, notificheAssociate, proprietario);

        this.setSogliaMinima(sogliaMinima);
        this.setOfferteRicevute(offerteRicevute);
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaTempoFisso offertaDaAggiungere) {        
        if (this.offerteRicevute == null)
            this.offerteRicevute = new HashSet<OffertaTempoFisso>();
        
        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaTempoFisso offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);

        if (this.offerteRicevute.isEmpty())
            this.offerteRicevute = null;
    }
}
