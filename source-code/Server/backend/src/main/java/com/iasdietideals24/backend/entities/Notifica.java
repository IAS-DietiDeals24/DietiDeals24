package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Notifica {
    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private String messaggio;

    private Account mittente;

    private Set<Account> destinatari;

    private Asta astaAssociata;

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
