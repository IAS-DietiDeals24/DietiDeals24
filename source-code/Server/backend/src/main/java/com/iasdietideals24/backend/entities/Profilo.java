package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Profilo {
    private String nomeUtente;

    private byte[] profilePicture;

    private String nome;

    private String cognome;

    private LocalDate dataNascita;

    private String areaGeografica;

    private String biografia;

    private String linkPersonale;

    private String linkInstagram;

    private String linkFacebook;

    private String linkGitHub;

    private String linkX;

    private Set<Account> accounts;

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
