package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "venditore")
public class Venditore extends Account {
    @OneToMany(mappedBy = "proprietario", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<AstaDiVenditore> astePossedute = new HashSet<>();

    @OneToMany(mappedBy = "venditoreCollegato", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<OffertaDiVenditore> offerteCollegate = new HashSet<>();

    // AllArgsConstructor
    public Venditore(String email, String password, TokensAccount tokensAccount, Profilo profilo) {
        super(email, password, tokensAccount, profilo);
    }

    // Metodi per astePossedute
    public void addAstaPosseduta(AstaDiVenditore astaDaAggiungere) {
        this.astePossedute.add(astaDaAggiungere);
    }

    public void removeAstaPosseduta(AstaDiVenditore astaDaRimuovere) {
        this.astePossedute.remove(astaDaRimuovere);
    }

    // Metodi per offerteCollegate
    public void addOffertaCollegata(OffertaDiVenditore offertaDaAggiungere) {
        this.offerteCollegate.add(offertaDaAggiungere);
    }

    public void removeOffertaCollegata(OffertaDiVenditore offertaDaRimuovere) {
        this.offerteCollegate.remove(offertaDaRimuovere);
    }

    @Override
    public String toString() {
        Iterator<AstaDiVenditore> itrAstaPosseduta = this.getAstePossedute().iterator();
        StringBuilder listIdAstePossedute = new StringBuilder();
        while (itrAstaPosseduta.hasNext()) {
            listIdAstePossedute.append(itrAstaPosseduta.next().getIdAsta()).append(", ");
        }

        Iterator<OffertaDiVenditore> itrOffertaCollegata = this.getOfferteCollegate().iterator();
        StringBuilder listIdOfferteCollegate = new StringBuilder();
        while (itrOffertaCollegata.hasNext()) {
            listIdOfferteCollegate.append(itrOffertaCollegata.next().getIdOfferta()).append(", ");
        }

        return "Venditore(astePossedute=" + listIdAstePossedute + ", offerteCollegate=" + listIdOfferteCollegate + ") is a " + super.toString();
    }
}
