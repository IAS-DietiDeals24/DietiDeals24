package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.exceptions.ParameterNotValidException;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
public class Profilo {
    @NonNull
    private String nomeUtente;

    @NonNull
    private byte[] profilePicture;

    @NonNull
    private String nome;

    @NonNull
    private String cognome;

    @NonNull
    private LocalDate dataNascita;

    private String areaGeografica;

    private String biografia;

    private String linkPersonale;

    private String linkInstagram;

    private String linkFacebook;

    private String linkGitHub;

    private String linkX;

    @NonNull
    @Setter(AccessLevel.NONE)
    private Set<Account> accounts;

    // AllArgsConstructor
    public Profilo(@NonNull String nomeUtente, @NonNull byte[] profilePicture, @NonNull String nome, @NonNull String cognome, @NonNull LocalDate dataNascita, String areaGeografica, String biografia, String linkPersonale, String linkInstagram, String linkFacebook, String linkGitHub, String linkX, @NonNull Account account) {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.areaGeografica = areaGeografica;
        this.biografia = biografia;
        this.linkPersonale = linkPersonale;
        this.linkInstagram = linkInstagram;
        this.linkFacebook = linkFacebook;
        this.linkGitHub = linkGitHub;
        this.linkX = linkX;

        this.addAccount(account);
        account.setProfilo(this);
    }

    // Creazione dell'account contestualmente al profilo
    public Profilo(@NonNull String nomeUtente, @NonNull byte[] profilePicture, @NonNull String nome, @NonNull String cognome, @NonNull LocalDate dataNascita, String areaGeografica, String biografia, String linkPersonale, String linkInstagram, String linkFacebook, String linkGitHub, String linkX, @NonNull String email, @NonNull String password, @NonNull String tipoAccount) throws ParameterNotValidException {
        this.nomeUtente = nomeUtente;
        this.profilePicture = profilePicture;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.areaGeografica = areaGeografica;
        this.biografia = biografia;
        this.linkPersonale = linkPersonale;
        this.linkInstagram = linkInstagram;
        this.linkFacebook = linkFacebook;
        this.linkGitHub = linkGitHub;
        this.linkX = linkX;

        if (tipoAccount.equals("Compratore"))
            this.addAccount(new Compratore(email, password, this));
        else if (tipoAccount.equals("Venditore"))
            this.addAccount(new Venditore(email, password, this));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profilo)) return false;
        Profilo profilo = (Profilo) o;
        return Objects.equals(this.nomeUtente, profilo.getNomeUtente()) && Objects.deepEquals(this.profilePicture, profilo.getProfilePicture()) && Objects.equals(this.nome, profilo.getNome()) && Objects.equals(this.cognome, profilo.getCognome()) && Objects.equals(this.dataNascita, profilo.getDataNascita()) && Objects.equals(this.areaGeografica, profilo.getAreaGeografica()) && Objects.equals(this.biografia, profilo.getBiografia()) && Objects.equals(this.linkPersonale, profilo.getLinkPersonale()) && Objects.equals(this.linkInstagram, profilo.getLinkInstagram()) && Objects.equals(this.linkFacebook, profilo.getLinkFacebook()) && Objects.equals(this.linkGitHub, profilo.getLinkGitHub()) && Objects.equals(this.linkX, profilo.getLinkX()) && Objects.equals(this.accounts, profilo.getAccounts());
    }
}
