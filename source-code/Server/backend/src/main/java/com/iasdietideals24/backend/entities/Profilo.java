package com.iasdietideals24.backend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iasdietideals24.backend.exceptions.ParameterNotValidException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Profilo {
    @Id
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

    @OneToMany(mappedBy = "profilo", cascade = CascadeType.ALL)
    @NonNull
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Account> accounts = new HashSet<>();

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
        this.accounts.add(accountDaAggiungere);
    }

    public void removeAccount(Account accountDaRimuovere) {
        this.accounts.remove(accountDaRimuovere);
    }

    // Metodi per recuperare un tipo di account
    public Compratore getCompratore() {
        Iterator<Account> itrAccount = this.getAccounts().iterator();
        Compratore compratore = null;
        while (itrAccount.hasNext()) {
            if (itrAccount instanceof Compratore)
                compratore = (Compratore) itrAccount.next();
            else
                itrAccount.next();
        }

        return compratore;
    }

    public Venditore getVenditore() {
        Iterator<Account> itrAccount = this.getAccounts().iterator();
        Venditore venditore = null;
        while (itrAccount.hasNext()) {
            if (itrAccount instanceof Venditore)
                venditore = (Venditore) itrAccount.next();
            else
                itrAccount.next();
        }

        return venditore;
    }

    @Override
    public String toString() {
        Iterator<Account> itrAccount = this.getAccounts().iterator();
        StringBuilder listEmailAccounts = new StringBuilder();
        listEmailAccounts.append("[");
        while (itrAccount.hasNext()) {
            listEmailAccounts.append(itrAccount.next().getEmail()).append(", ");
        }
        listEmailAccounts.append("]");

        return "Profilo(nomeUtente=" + this.getNomeUtente() + ", profilePicture=" + java.util.Arrays.toString(this.getProfilePicture()) + ", nome=" + this.getNome() + ", cognome=" + this.getCognome() + ", dataNascita=" + this.getDataNascita() + ", areaGeografica=" + this.getAreaGeografica() + ", biografia=" + this.getBiografia() + ", linkPersonale=" + this.getLinkPersonale() + ", linkInstagram=" + this.getLinkInstagram() + ", linkFacebook=" + this.getLinkFacebook() + ", linkGitHub=" + this.getLinkGitHub() + ", linkX=" + this.getLinkX() + ", accounts=" + listEmailAccounts + ")";
    }
}
