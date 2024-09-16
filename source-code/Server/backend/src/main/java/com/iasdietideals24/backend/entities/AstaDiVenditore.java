package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoAsta")
public abstract class AstaDiVenditore extends Asta {
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "fk_venditore_for_astadivenditore")
    @NonNull
    private Venditore proprietario;

    // AllArgsConstructor
    public AstaDiVenditore(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine);

        this.proprietario = proprietario;
        proprietario.addAstaPosseduta(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaDiVenditore)) return false;
        if (!super.equals(o)) return false;
        AstaDiVenditore asta = (AstaDiVenditore) o;
        return Objects.equals(this.proprietario, asta.getProprietario());
    }
}
