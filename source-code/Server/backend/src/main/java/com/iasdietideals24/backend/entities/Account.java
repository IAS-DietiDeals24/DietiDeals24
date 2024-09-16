package com.iasdietideals24.backend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public abstract class Account {
    @Id
    @NonNull
    private String email;

    @NonNull
    private String password;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fk_profilo_for_account")
    @NonNull
    private Profilo profilo;

    @OneToMany(mappedBy = "mittente", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<Notifica> notificheInviate = new HashSet<Notifica>();

    @ManyToMany(mappedBy="destinatari", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<Notifica> notificheRicevute = new HashSet<Notifica>();

    // AllArgsConstructor
    public Account(@NonNull String email, @NonNull String password, @NonNull Profilo profilo) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(this.email, account.getEmail()) && Objects.equals(this.password, account.getPassword()) && Objects.equals(this.profilo, account.getProfilo()) && Objects.equals(this.notificheInviate, account.getNotificheInviate()) && Objects.equals(this.notificheRicevute, account.getNotificheRicevute());
    }
}
