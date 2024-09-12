package com.iasdietideals24.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AstaDiCompratore extends Asta {
    @NonNull private Compratore proprietario;

    // AllArgsConstructor
    public AstaDiCompratore (String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, Set<byte[]> immagini, Set<Notifica> notificheAssociate, Compratore proprietario) {
        super(categoria, nome, descrizione, dataScadenza, oraScadenza, immagini, notificheAssociate);

        this.setProprietario(proprietario);
    }
}
