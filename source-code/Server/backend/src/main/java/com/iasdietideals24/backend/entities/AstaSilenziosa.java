package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AstaSilenziosa extends AstaDiVenditore {
    private Set<OffertaSilenziosa> offerteRicevute;

    // AllArgsConstructor
    public AstaSilenziosa(String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, Set<byte[]> immagini, Set<Notifica> notificheAssociate, Venditore proprietario, Set<OffertaSilenziosa> offerteRicevute) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagini, notificheAssociate, proprietario);

        this.setOfferteRicevute(offerteRicevute);
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaSilenziosa offertaDaAggiungere) {        
        if (this.offerteRicevute == null)
            this.offerteRicevute = new LinkedHashSet<OffertaSilenziosa>();
        
        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaSilenziosa offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);

        if (this.offerteRicevute.isEmpty())
            this.offerteRicevute = null;
    }
}
