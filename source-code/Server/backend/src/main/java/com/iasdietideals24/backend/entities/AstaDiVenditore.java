package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class AstaDiVenditore extends Asta {
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
