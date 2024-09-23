package com.iasdietideals24.backend.entities.utilities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class AnagraficaProfilo {

    @NonNull
    private String nome;

    @NonNull
    private String cognome;

    @NonNull
    private LocalDate dataNascita;

    private String areaGeografica;

    private String genere;

    private String biografia;

    // AllArgsConstructor
    public AnagraficaProfilo(@NonNull String nome, @NonNull String cognome, @NonNull LocalDate dataNascita, String areaGeografica, String genere, String biografia) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.areaGeografica = areaGeografica;
        this.genere = genere;
        this.biografia = biografia;
    }

    @Override
    public String toString() {
        return "AnagraficaProfilo(nome=" + this.getNome() + ", cognome=" + this.getCognome() + ", dataNascita=" + this.getDataNascita() + ", areaGeografica=" + this.getAreaGeografica() + ", biografia=" + this.getBiografia() + ")";
    }
}
