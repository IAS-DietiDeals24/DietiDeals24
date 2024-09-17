package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoAsta")
public abstract class AstaDiCompratore extends Asta {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_compratore_email")
    @NonNull
    private Compratore proprietario;

    // AllArgsConstructor
    public AstaDiCompratore(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Compratore proprietario) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine);

        this.proprietario = proprietario;
        proprietario.addAstaPosseduta(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AstaDiCompratore)) return false;
        if (!super.equals(o)) return false;
        AstaDiCompratore asta = (AstaDiCompratore) o;
        return Objects.equals(this.proprietario, asta.getProprietario());
    }

    @Override
    public String toString() {
        return "AstaDiCompratore(proprietario=" + this.getProprietario().getEmail() + ") is a " + super.toString();
    }
}
