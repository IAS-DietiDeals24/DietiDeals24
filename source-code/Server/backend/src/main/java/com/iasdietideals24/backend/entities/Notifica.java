package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "notifica")
@Check(constraints = "data_invio <= NOW()")
public class Notifica {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifica_id_seq")
    @Column(name = "id_notifica", nullable = false)
    private Long idNotifica;

    @NonNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_invio", nullable = false)
    private LocalDate dataInvio;

    @NonNull
    @Temporal(TemporalType.TIME)
    @Column(name = "ora_invio", nullable = false)
    private LocalTime oraInvio;

    @NonNull
    @Column(name = "messaggio", nullable = false)
    private String messaggio;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "account_id_account", nullable = false)
    @NonNull
    private Account mittente;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "destinatari",
            joinColumns = {@JoinColumn(name = "notifica_id_notifica", nullable = false, foreignKey = @ForeignKey(name = "fk_notifica_id_notifica"))},
            inverseJoinColumns = {@JoinColumn(name = "account_id_account", nullable = false)})
    @NonNull
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Account> destinatari = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "asta_id_asta", nullable = false)
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
            listEmailDestinatari.append(itrDestinatario.next().getIdAccount()).append(", ");
        }
        listEmailDestinatari.append("]");

        return "Notifica(idNotifica=" + this.getIdNotifica() + ", dataInvio=" + this.getDataInvio() + ", oraInvio=" + this.getOraInvio() + ", messaggio=" + this.getMessaggio() + ", mittente=" + this.getMittente().getIdAccount() + ", destinatari=" + listEmailDestinatari + ", astaAssociata=" + this.getAstaAssociata().getIdAsta() + ")";
    }
}
