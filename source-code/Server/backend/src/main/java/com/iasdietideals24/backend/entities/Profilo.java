package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Slf4j
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "profilo")
public class Profilo {
    @Id
    @NonNull
    @Column(name = "nome_utente", nullable = false)
    private String nomeUtente;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Embedded
    @NonNull
    private AnagraficaProfilo anagrafica;

    @Embedded
    private LinksProfilo links;

    @OneToMany(mappedBy = "profilo", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @NonNull
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Account> accounts = new HashSet<>();

    // AllArgsConstructor
    public Profilo(@NonNull String nomeUtente, byte[] profilePicture, @NonNull AnagraficaProfilo anagraficaProfilo, LinksProfilo linksProfilo, @NonNull Account account) {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.anagrafica = anagraficaProfilo;
        this.links = linksProfilo;

        this.addAccount(account);
        account.setProfilo(this);
    }

    // Creazione dell'account contestualmente al profilo
    public Profilo(@NonNull String nomeUtente, byte[] profilePicture, @NonNull AnagraficaProfilo anagraficaProfilo, LinksProfilo linksProfilo, @NonNull String email, @NonNull String password, TokensAccount tokensAccount, @NonNull String tipoAccount) throws InvalidTypeException {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.anagrafica = anagraficaProfilo;
        this.links = linksProfilo;

        if (tipoAccount.equals(Compratore.class.getSimpleName()))
            this.addAccount(new Compratore(email, password, tokensAccount, this));
        else if (tipoAccount.equals(Venditore.class.getSimpleName()))
            this.addAccount(new Venditore(email, password, tokensAccount, this));
        else {
            log.warn("Il tipo account '{}' non corrisponde a nessun tipo esistente!", tipoAccount);
            throw new InvalidTypeException("Il tipo account '" + tipoAccount + "' non corrisponde a nessun tipo esistente!");
        }
    }

    // Metodi per accounts
    public void addAccount(Account accountDaAggiungere) {
        this.accounts.add(accountDaAggiungere);
    }

    public void removeAccount(Account accountDaRimuovere) {
        this.accounts.remove(accountDaRimuovere);
    }

    // Metodi per recuperare un tipo di account
    public Compratore getCompratore() {
        Iterator<Account> itrAccount = this.getAccounts().iterator();
        log.trace("Account trovati: {}", this.getAccounts());

        Account account;
        Compratore compratore = null;
        while (compratore == null && itrAccount.hasNext()) {
            account = itrAccount.next();
            if (account instanceof Compratore c) {
                log.trace("Compratore trovato");
                compratore = c;
            }
        }

        log.trace("Compratore selezionato: {}", compratore);
        return compratore;
    }

    public Venditore getVenditore() {
        Iterator<Account> itrAccount = this.getAccounts().iterator();
        log.trace("Account trovati: {}", this.getAccounts());

        Account account;
        Venditore venditore = null;
        while (venditore == null && itrAccount.hasNext()) {
            account = itrAccount.next();
            if (account instanceof Venditore v) {
                log.trace("Venditore trovato");
                venditore = v;
            }
        }

        log.trace("Venditore selezionato: {}", venditore);
        return venditore;
    }

    @Override
    public String toString() {
        Iterator<Account> itrAccount = this.getAccounts().iterator();
        StringBuilder listEmailAccounts = new StringBuilder();
        listEmailAccounts.append("[");
        while (itrAccount.hasNext()) {
            listEmailAccounts.append(itrAccount.next().getIdAccount()).append(", ");
        }
        listEmailAccounts.append("]");

        return "Profilo(nomeUtente=" + this.getNomeUtente() + ", profilePicture=" + java.util.Arrays.toString(this.getProfilePicture()) + ", anagrafica=" + this.getAnagrafica() + ", links=" + this.getLinks() + ", accounts=" + listEmailAccounts + ")";
    }
}
