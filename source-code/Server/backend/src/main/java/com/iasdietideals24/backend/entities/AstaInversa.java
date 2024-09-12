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
public class AstaInversa extends AstaDiCompratore {
    @NonNull private BigDecimal sogliaIniziale;

    private Set<OffertaInversa> offerteRicevute;

    // AllArgsConstructor
    public AstaInversa(String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, Set<byte[]> immagini, Set<Notifica> notificheAssociate, Compratore proprietario, BigDecimal sogliaIniziale, Set<OffertaInversa> offerteRicevute) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagini, notificheAssociate, proprietario);

        this.setSogliaIniziale(sogliaIniziale);
        this.setOfferteRicevute(offerteRicevute);
    }

    // Metodi per offerteRicevute
    public void addOffertaRicevuta(OffertaInversa offertaDaAggiungere) {        
        if (this.offerteRicevute == null)
            this.offerteRicevute = new HashSet<OffertaInversa>();

        this.offerteRicevute.add(offertaDaAggiungere);
    }

    public void removeOffertaRicevuta(OffertaInversa offertaDaRimuovere) {
        this.offerteRicevute.remove(offertaDaRimuovere);

        if (this.offerteRicevute.isEmpty())
            this.offerteRicevute = null;
    }
}
