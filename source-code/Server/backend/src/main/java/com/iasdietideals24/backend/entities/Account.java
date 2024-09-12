package com.iasdietideals24.backend.entities;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public abstract class Account {
    @NonNull private String email;
    
    @NonNull private String password;
    
    @NonNull private Profilo profilo;
    
    private Set<Notifica> notificheInviate;

    private Set<Notifica> notificheRicevute;

    // Metodi per notificheInviate
    public void addNotificaInviata(Notifica notificaInviataDaAggiungere) {        
        if (this.notificheInviate == null)
            this.notificheInviate = new HashSet<Notifica>();

        this.notificheInviate.add(notificaInviataDaAggiungere);
    }

    public void removeNotificaInviata(Notifica notificaInviataDaRimuovere) {
        this.notificheInviate.remove(notificaInviataDaRimuovere);

        if (this.notificheInviate.isEmpty())
            this.notificheInviate = null;
    }

    // Metodi per notificheRicevute
    public void addNotificaRicevuta(Notifica notificaRicevutaDaAggiungere) {        
        if (this.notificheRicevute == null)
            this.notificheRicevute = new HashSet<Notifica>();

        this.notificheRicevute.add(notificaRicevutaDaAggiungere);
    }

    public void removeNotificaRicevuta(Notifica notificaRicevutaDaRimuovere) {
        this.notificheRicevute.remove(notificaRicevutaDaRimuovere);

        if (this.notificheRicevute.isEmpty())
            this.notificheRicevute = null;
    }
}
