package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public abstract class Asta {
    @NonNull private String categoria;

    @NonNull private String nome;

    @NonNull private String descrizione;

    @NonNull private LocalDate dataScadenza;

    @NonNull private LocalTime oraScadenza;

    private Set<byte[]> immagini;

    private Set<Notifica> notificheAssociate;

    // Metodi per immagini
    public void addImmagine(byte[] immagineDaAggiungere) {        
        if (this.immagini == null)
            this.immagini = new HashSet<byte[]>();

        this.immagini.add(immagineDaAggiungere);
    }

    public void removeImmagine(byte[] immagineDaRimuovere) {
        this.immagini.remove(immagineDaRimuovere);

        if (this.immagini.isEmpty())
            this.immagini = null;
    }

    // Metodi per notificheAssociate
    public void addNotificaAssociata(Notifica notificaDaAggiungere) {        
        if (this.notificheAssociate == null)
            this.notificheAssociate = new HashSet<Notifica>();

        this.notificheAssociate.add(notificaDaAggiungere);
    }

    public void removeNotificaAssociata(Notifica notificaDaRimuovere) {
        this.notificheAssociate.remove(notificaDaRimuovere);

        if (this.notificheAssociate.isEmpty())
            this.notificheAssociate = null;
    }
}
