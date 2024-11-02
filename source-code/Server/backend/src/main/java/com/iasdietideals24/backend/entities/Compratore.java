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
@Entity(name = "compratore")
public class Compratore extends Account {
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<AstaDiCompratore> astePossedute = new HashSet<>();

    @OneToMany(mappedBy = "compratoreCollegato", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<OffertaDiCompratore> offerteCollegate = new HashSet<>();

    // AllArgsConstructor
    public Compratore(String email, String password, TokensAccount tokensAccount, Profilo profilo) {
        super(email, password, tokensAccount, profilo);
    }

    // Metodi per astePossedute
    public void addAstaPosseduta(AstaDiCompratore astaDaAggiungere) {
        this.astePossedute.add(astaDaAggiungere);
    }

    public void removeAstaPosseduta(AstaDiCompratore astaDaRimuovere) {
        this.astePossedute.remove(astaDaRimuovere);
    }

    // Metodi per offerteCollegate
    public void addOffertaCollegata(OffertaDiCompratore offertaDaAggiungere) {
        this.offerteCollegate.add(offertaDaAggiungere);
    }

    public void removeOffertaCollegata(OffertaDiCompratore offertaDaRimuovere) {
        this.offerteCollegate.remove(offertaDaRimuovere);
    }

    @Override
    public String toString() {
        Iterator<AstaDiCompratore> itrAstaPosseduta = this.getAstePossedute().iterator();
        StringBuilder listIdAstePossedute = new StringBuilder();
        listIdAstePossedute.append("[");
        while (itrAstaPosseduta.hasNext()) {
            listIdAstePossedute.append(itrAstaPosseduta.next().getIdAsta()).append(", ");
        }
        listIdAstePossedute.append("]");

        Iterator<OffertaDiCompratore> itrOffertaCollegata = this.getOfferteCollegate().iterator();
        StringBuilder listIdOfferteCollegate = new StringBuilder();
        listIdOfferteCollegate.append("[");
        while (itrOffertaCollegata.hasNext()) {
            listIdOfferteCollegate.append(itrOffertaCollegata.next().getIdOfferta()).append(", ");
        }
        listIdOfferteCollegate.append("]");

        return "Compratore(astePossedute=" + listIdAstePossedute + ", offerteCollegate=" + listIdOfferteCollegate + ") is a " + super.toString();
    }
}
