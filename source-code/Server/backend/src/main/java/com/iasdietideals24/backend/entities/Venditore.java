package com.iasdietideals24.backend.entities;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Venditore extends Account {
    private Set<AstaDiVenditore> astePossedute;
    
    private Set<OffertaDiVenditore> offerteCollegate;

    // AllArgsConstructor
    public Venditore (String email, String password, Profilo profilo, Set<Notifica> notificheInviate, Set<Notifica> notificheRicevute, Set<AstaDiVenditore> astePossedute, Set<OffertaDiVenditore> offerteCollegate) {
        super(email, password, profilo, notificheInviate, notificheRicevute);

        this.setAstePossedute(astePossedute);
        this.setOfferteCollegate(offerteCollegate);
    }

    // Metodi per astePossedute
    public void addAstaPosseduta(AstaDiVenditore astaDaAggiungere) {        
        if (this.astePossedute == null)
            this.astePossedute = new HashSet<AstaDiVenditore>();

        this.astePossedute.add(astaDaAggiungere);
    }

    public void removeAstaPosseduta(AstaDiVenditore astaDaRimuovere) {
        this.astePossedute.remove(astaDaRimuovere);

        if (this.astePossedute.isEmpty())
            this.astePossedute = null;
    }

    // Metodi per offerteCollegate
    public void addOffertaCollegata(OffertaDiVenditore offertaDaAggiungere) {        
        if (this.offerteCollegate == null)
            this.offerteCollegate = new HashSet<OffertaDiVenditore>();
        
        this.offerteCollegate.add(offertaDaAggiungere);
    }

    public void removeOffertaCollegata(OffertaDiVenditore offertaDaRimuovere) {
        this.offerteCollegate.remove(offertaDaRimuovere);

        if (this.offerteCollegate.isEmpty())
            this.offerteCollegate = null;
    }
}
