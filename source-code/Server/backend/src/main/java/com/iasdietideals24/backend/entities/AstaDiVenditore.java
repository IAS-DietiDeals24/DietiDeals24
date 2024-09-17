package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoAsta")
public abstract class AstaDiVenditore extends Asta {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_venditore_email")
    @NonNull
    private Venditore proprietario;

    // AllArgsConstructor
    protected AstaDiVenditore(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine);

        this.proprietario = proprietario;
        proprietario.addAstaPosseduta(this);
    }

    @Override
    public String toString() {
        return "AstaDiVenditore(proprietario=" + this.getProprietario().getEmail() + ") + is a " + super.toString();
    }
}
