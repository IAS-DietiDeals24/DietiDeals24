package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {
    @Id
    @NonNull
    private String email;

    @NonNull
    private String password;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_profilo_nomeutente")
    @NonNull
    private Profilo profilo;

    @OneToMany(mappedBy = "mittente", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Notifica> notificheInviate = new HashSet<>();

    @ManyToMany(mappedBy = "destinatari", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Notifica> notificheRicevute = new HashSet<>();

    // AllArgsConstructor
    protected Account(@NonNull String email, @NonNull String password, @NonNull Profilo profilo) {
        this.email = email;
        this.password = password;
        this.profilo = profilo;
    }

    // Metodi per notificheInviate
    public void addNotificaInviata(@NonNull Notifica notificaInviataDaAggiungere) {
        this.notificheInviate.add(notificaInviataDaAggiungere);
    }

    public void removeNotificaInviata(@NonNull Notifica notificaInviataDaRimuovere) {
        this.notificheInviate.remove(notificaInviataDaRimuovere);
    }

    // Metodi per notificheRicevute
    public void addNotificaRicevuta(@NonNull Notifica notificaRicevutaDaAggiungere) {
        this.notificheRicevute.add(notificaRicevutaDaAggiungere);
    }

    public void removeNotificaRicevuta(@NonNull Notifica notificaRicevutaDaRimuovere) {
        this.notificheRicevute.remove(notificaRicevutaDaRimuovere);
    }

    @Override
    public String toString() {
        Iterator<Notifica> itrNotificaInviata = this.getNotificheInviate().iterator();
        StringBuilder listIdNotificheInviate = new StringBuilder();
        listIdNotificheInviate.append("[");
        while (itrNotificaInviata.hasNext()) {
            listIdNotificheInviate.append(itrNotificaInviata.next().getIdNotifica()).append(", ");
        }
        listIdNotificheInviate.append("]");

        Iterator<Notifica> itrNotificaRicevuta = this.getNotificheRicevute().iterator();
        StringBuilder listIdNotificheRicevute = new StringBuilder();
        listIdNotificheRicevute.append("[");
        while (itrNotificaRicevuta.hasNext()) {
            listIdNotificheRicevute.append(itrNotificaRicevuta.next().getIdNotifica()).append(", ");
        }
        listIdNotificheRicevute.append("]");

        return "Account(email=" + this.getEmail() + ", password=" + this.getPassword() + ", profilo=" + this.getProfilo().getNomeUtente() + ", notificheInviate=" + listIdNotificheInviate + ", notificheRicevute=" + listIdNotificheRicevute + ")";
    }
}
