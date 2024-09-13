package com.iasdietideals24.backend.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
public class Notifica {
    @NonNull
    private LocalDate dataInvio;

    @NonNull
    private LocalTime oraInvio;

    @NonNull
    private String messaggio;

    @NonNull
    private Account mittente;

    @NonNull
    @Setter(AccessLevel.NONE)
    private Set<Account> destinatari;

    @NonNull
    private Asta astaAssociata;

    // AllArgsConstructor
    public Notifica(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull String messaggio, @NonNull Account mittente, @NonNull Account destinatario, @NonNull Asta astaAssociata) {
        this.dataInvio = dataInvio;
        this.oraInvio = oraInvio;
        this.messaggio = messaggio;

        this.mittente = mittente;
        mittente.addNotificaInviata(this);

        this.addDestinatario(destinatario);
        destinatario.addNotificaRicevuta(this);

        this.astaAssociata = astaAssociata;
        astaAssociata.addNotificaAssociata(this);
    }

    // Metodi per destinatari
    public void addDestinatario(@NonNull Account destinatarioDaAggiungere) {
        if (this.destinatari == null)
            this.destinatari = new HashSet<Account>();

        this.destinatari.add(destinatarioDaAggiungere);
    }

    public void removeDestinatario(@NonNull Account destinatarioDaRimuovere) {
        this.destinatari.remove(destinatarioDaRimuovere);

        if (this.destinatari.isEmpty())
            this.destinatari = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notifica)) return false;
        Notifica notifica = (Notifica) o;
        return Objects.equals(this.dataInvio, notifica.getDataInvio()) && Objects.equals(this.oraInvio, notifica.getOraInvio()) && Objects.equals(this.messaggio, notifica.getMessaggio()) && Objects.equals(this.mittente, notifica.getMittente()) && Objects.equals(this.destinatari, notifica.getDestinatari()) && Objects.equals(this.astaAssociata, notifica.getAstaAssociata());
    }
}
