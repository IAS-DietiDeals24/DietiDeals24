package com.iasdietideals24.backend.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Compratore extends Account {
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<AstaDiCompratore> astePossedute = new HashSet<AstaDiCompratore>();

    @OneToMany(mappedBy = "compratoreCollegato", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<OffertaDiCompratore> offerteCollegate = new HashSet<OffertaDiCompratore>();

    // AllArgsConstructor
    public Compratore (String email, String password, Profilo profilo) {
        super(email, password, profilo);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compratore)) return false;
        if (!super.equals(o)) return false;
        Compratore compratore = (Compratore) o;
        return Objects.equals(this.astePossedute, compratore.getAstePossedute()) && Objects.equals(this.offerteCollegate, compratore.getOfferteCollegate());
    }
}
