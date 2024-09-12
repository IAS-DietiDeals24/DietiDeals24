package com.iasdietideals24.backend.entities;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Compratore extends Account {
    private Set<AstaDiCompratore> astePossedute;
    
    private Set<OffertaDiCompratore> offerteCollegate;

    // AllArgsConstructor
    public Compratore (String email, String password, Profilo profilo, Set<Notifica> notificheInviate, Set<Notifica> notificheRicevute, Set<AstaDiCompratore> astePossedute, Set<OffertaDiCompratore> offerteCollegate) {
        super(email, password, profilo, notificheInviate, notificheRicevute);

        this.setAstePossedute(astePossedute);
        this.setOfferteCollegate(offerteCollegate);
    }

    // Metodi per astePossedute
    public void addAstaPosseduta(AstaDiCompratore astaDaAggiungere) {        
        if (this.astePossedute == null)
            this.astePossedute = new HashSet<AstaDiCompratore>();

        this.astePossedute.add(astaDaAggiungere);
    }

    public void removeAstaPosseduta(AstaDiCompratore astaDaRimuovere) {
        this.astePossedute.remove(astaDaRimuovere);

        if (this.astePossedute.isEmpty())
            this.astePossedute = null;
    }

    // Metodi per offerteCollegate
    public void addOffertaCollegata(OffertaDiCompratore offertaDaAggiungere) {        
        if (this.offerteCollegate == null)
            this.offerteCollegate = new HashSet<OffertaDiCompratore>();
        
        this.offerteCollegate.add(offertaDaAggiungere);
    }

    public void removeOffertaCollegata(OffertaDiCompratore offertaDaRimuovere) {
        this.offerteCollegate.remove(offertaDaRimuovere);

        if (this.offerteCollegate.isEmpty())
            this.offerteCollegate = null;
    }
}
