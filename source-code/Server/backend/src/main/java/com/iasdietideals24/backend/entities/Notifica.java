package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Notifica {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifica_id_seq")
    @NonNull
    private Long idNotifica;

    @NonNull
    private LocalDate dataInvio;

    @NonNull
    private LocalTime oraInvio;

    @NonNull
    private String messaggio;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fk_account_for_notifica")
    @NonNull
    private Account mittente;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "destinatari",
            joinColumns = { @JoinColumn(name = "fk_notifica_for_account") },
            inverseJoinColumns = { @JoinColumn(name = "fk_account_for_notifica") })
    @NonNull
    @Setter(AccessLevel.NONE)
    private Set<Account> destinatari;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fk_asta_for_notifica")
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
