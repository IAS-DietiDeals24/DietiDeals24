package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notifica {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifica_id_seq")
    private Long idNotifica;

    @NonNull
    private LocalDate dataInvio;

    @NonNull
    private LocalTime oraInvio;

    @NonNull
    private String messaggio;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_account_email")
    @NonNull
    private Account mittente;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "destinatari",
            joinColumns = {@JoinColumn(name = "fk_notifica_idnotifica")},
            inverseJoinColumns = {@JoinColumn(name = "fk_account_email")})
    @NonNull
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Account> destinatari = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_asta_idasta")
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
        this.destinatari.add(destinatarioDaAggiungere);
    }

    public void removeDestinatario(@NonNull Account destinatarioDaRimuovere) {
        this.destinatari.remove(destinatarioDaRimuovere);
    }

    @Override
    public String toString() {
        Iterator<Account> itrDestinatario = this.getDestinatari().iterator();
        StringBuilder listEmailDestinatari = new StringBuilder();
        listEmailDestinatari.append("[");
        while (itrDestinatario.hasNext()) {
            listEmailDestinatari.append(itrDestinatario.next().getEmail()).append(", ");
        }
        listEmailDestinatari.append("]");

        return "Notifica(idNotifica=" + this.getIdNotifica() + ", dataInvio=" + this.getDataInvio() + ", oraInvio=" + this.getOraInvio() + ", messaggio=" + this.getMessaggio() + ", mittente=" + this.getMittente().getEmail() + ", destinatari=" + listEmailDestinatari + ", astaAssociata=" + this.getAstaAssociata().getIdAsta() + ")";
    }
}
