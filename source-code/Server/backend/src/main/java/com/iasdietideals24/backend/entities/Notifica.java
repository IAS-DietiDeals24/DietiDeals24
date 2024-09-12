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
public class Notifica {
    @NonNull private LocalDate dataInvio;

    @NonNull private LocalTime oraInvio;

    @NonNull private String messaggio;

    @NonNull private Account mittente;

    @NonNull private Set<Account> destinatari;

    @NonNull private Asta astaAssociata;

    // Metodi per destinatari
    public void addDestinatario(Account destinatarioDaAggiungere) {        
        if (this.destinatari == null)
            this.destinatari = new HashSet<Account>();
        
        this.destinatari.add(destinatarioDaAggiungere);
    }

    public void removeDestinatario(Account destinatarioDaRimuovere) {
        this.destinatari.remove(destinatarioDaRimuovere);

        if (this.destinatari.isEmpty())
            this.destinatari = null;
    }
}
