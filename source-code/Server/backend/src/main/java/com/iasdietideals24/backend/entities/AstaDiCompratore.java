package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "asta_di_compratore")
public abstract class AstaDiCompratore extends Asta {
    @ManyToOne
    @JoinColumn(name = "compratore_email", nullable = false)
    @NonNull
    private Compratore proprietario;

    // AllArgsConstructor
    protected AstaDiCompratore(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Compratore proprietario) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine);

        this.proprietario = proprietario;
        proprietario.addAstaPosseduta(this);
    }

    @Override
    public String toString() {
        return "AstaDiCompratore(proprietario=" + this.getProprietario().getEmail() + ") is a " + super.toString();
    }
}
