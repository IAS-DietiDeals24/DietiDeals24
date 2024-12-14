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
@Entity(name = "asta_di_compratore")
public abstract class AstaDiCompratore extends Asta {
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "compratore_id_account", nullable = false, foreignKey = @ForeignKey(name = "fk_compratore_id_account"))
    @NonNull
    private Compratore proprietario;

    // AllArgsConstructor
    protected AstaDiCompratore(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, @NonNull Compratore proprietario, StatoAsta statoAsta) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, statoAsta);

        this.proprietario = proprietario;
        proprietario.addAstaPosseduta(this);
    }

    @Override
    public String toString() {
        return "AstaDiCompratore(proprietario=" + this.getProprietario().getIdAccount() + ") is a " + super.toString();
    }
}
