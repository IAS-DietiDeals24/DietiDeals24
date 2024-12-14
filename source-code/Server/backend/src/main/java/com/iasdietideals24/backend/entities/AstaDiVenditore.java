package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "asta_di_venditore")
public abstract class AstaDiVenditore extends Asta {
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "venditore_id_account", nullable = false, foreignKey = @ForeignKey(name = "fk_venditore_id_account"))
    @NonNull
    private Venditore proprietario;

    // AllArgsConstructor
    protected AstaDiVenditore(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Venditore proprietario, StatoAsta statoAsta) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, statoAsta);

        this.proprietario = proprietario;
        proprietario.addAstaPosseduta(this);
    }

    @Override
    public String toString() {
        return "AstaDiVenditore(proprietario=" + this.getProprietario().getIdAccount() + ") + is a " + super.toString();
    }
}
