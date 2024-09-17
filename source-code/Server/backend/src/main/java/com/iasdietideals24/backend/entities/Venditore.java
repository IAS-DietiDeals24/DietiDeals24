package com.iasdietideals24.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Venditore extends Account {
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<AstaDiVenditore> astePossedute = new HashSet<AstaDiVenditore>();

    @OneToMany(mappedBy = "venditoreCollegato", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<OffertaDiVenditore> offerteCollegate = new HashSet<OffertaDiVenditore>();

    // AllArgsConstructor
    public Venditore(String email, String password, Profilo profilo) {
        super(email, password, profilo);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venditore)) return false;
        if (!super.equals(o)) return false;
        Venditore venditore = (Venditore) o;
        return Objects.equals(this.astePossedute, venditore.getAstePossedute()) && Objects.equals(this.offerteCollegate, venditore.getOfferteCollegate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), astePossedute, offerteCollegate);
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
