package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.iasdietideals24.backend.exceptions.ParameterNotValidException;

import lombok.Data;
import lombok.NonNull;

@Data
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

    @NonNull private Set<Account> accounts = new HashSet<Account>();

    //AllArgsConstructor
    public Profilo(String nomeUtente, byte[] profilePicture, String nome, String cognome, LocalDate dataNascita, String areaGeografica, String biografia, String linkPersonale, String linkInstagram, String linkFacebook, String linkGitHub, String linkX, Account account){
        this.setNomeUtente(nomeUtente);
        this.setProfilePicture(profilePicture);
        this.setNome(nome);
        this.setCognome(cognome);
        this.setDataNascita(dataNascita);
        this.setAreaGeografica(areaGeografica);
        this.setBiografia(biografia);
        this.setLinkPersonale(linkPersonale);
        this.setLinkInstagram(linkInstagram);
        this.setLinkFacebook(linkFacebook);
        this.setLinkGitHub(linkGitHub);
        this.setLinkX(linkX);

        this.accounts.add(account);
        account.setProfilo(this);
    }

    // Creazione dell'account contestualmente al profilo
    public Profilo(String nomeUtente, byte[] profilePicture, String nome, String cognome, LocalDate dataNascita, String areaGeografica, String biografia, String linkPersonale, String linkInstagram, String linkFacebook, String linkGitHub, String linkX, String email, String password, String tipoAccount) throws ParameterNotValidException{
        this.setNomeUtente(nomeUtente);
        this.setProfilePicture(profilePicture);
        this.setNome(nome);
        this.setCognome(cognome);
        this.setDataNascita(dataNascita);
        this.setAreaGeografica(areaGeografica);
        this.setBiografia(biografia);
        this.setLinkPersonale(linkPersonale);
        this.setLinkInstagram(linkInstagram);
        this.setLinkFacebook(linkFacebook);
        this.setLinkGitHub(linkGitHub);
        this.setLinkX(linkX);

        if (tipoAccount.equals("Compratore"))
            this.accounts.add(new Compratore(email, password, this, null, null, null, null));
        else if (tipoAccount.equals("Venditore"))
            this.accounts.add(new Venditore(email, password, this, null, null, null, null));
        else
            throw new ParameterNotValidException();
    }

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
