package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Profilo {
    @NonNull private String nomeUtente;

    @NonNull private byte[] profilePicture;

    @NonNull private String nome;

    @NonNull private String cognome;

    @NonNull private LocalDate dataNascita;

    private String areaGeografica;

    private String biografia;

    private String linkPersonale;

    private String linkInstagram;

    private String linkFacebook;

    private String linkGitHub;

    private String linkX;

    @NonNull private Set<Account> accounts;

    // Metodi per accounts
    public void addAccount(Account accountDaAggiungere) {        
        if (this.accounts == null)
            this.accounts = new HashSet<Account>();
        
        this.accounts.add(accountDaAggiungere);
    }

    public void removeAccount(Account accountDaRimuovere) {
        this.accounts.remove(accountDaRimuovere);

        if (this.accounts.isEmpty())
            this.accounts = null;
    }
}
