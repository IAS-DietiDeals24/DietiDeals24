package com.iasdietideals24.backend.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Venditore extends Account {
    @OneToMany(mappedBy = "proprietario")
    @Setter(AccessLevel.NONE)
    private Set<AstaDiVenditore> astePossedute;

    @OneToMany(mappedBy = "venditoreCollegato")
    @Setter(AccessLevel.NONE)
    private Set<OffertaDiVenditore> offerteCollegate;

    // AllArgsConstructor
    public Venditore (String email, String password, Profilo profilo) {
        super(email, password, profilo);
    }

    // Metodi per astePossedute
    public void addAstaPosseduta(AstaDiVenditore astaDaAggiungere) {        
        if (this.astePossedute == null)
            this.astePossedute = new HashSet<AstaDiVenditore>();

        this.astePossedute.add(astaDaAggiungere);
    }

    public void removeAstaPosseduta(AstaDiVenditore astaDaRimuovere) {
        this.astePossedute.remove(astaDaRimuovere);

        if (this.astePossedute.isEmpty())
            this.astePossedute = null;
    }

    // Metodi per offerteCollegate
    public void addOffertaCollegata(OffertaDiVenditore offertaDaAggiungere) {        
        if (this.offerteCollegate == null)
            this.offerteCollegate = new HashSet<OffertaDiVenditore>();
        
        this.offerteCollegate.add(offertaDaAggiungere);
    }

    public void removeOffertaCollegata(OffertaDiVenditore offertaDaRimuovere) {
        this.offerteCollegate.remove(offertaDaRimuovere);

        if (this.offerteCollegate.isEmpty())
            this.offerteCollegate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venditore)) return false;
        if (!super.equals(o)) return false;
        Venditore venditore = (Venditore) o;
        return Objects.equals(this.astePossedute, venditore.getAstePossedute()) && Objects.equals(this.offerteCollegate, venditore.getOfferteCollegate());
    }
}
