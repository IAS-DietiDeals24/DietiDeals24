package com.iasdietideals24.backend.entities.utilities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Embeddable
@Check(constraints = "data_nascita <= NOW()")
public class AnagraficaProfilo {

    @NonNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NonNull
    @Column(name = "cognome", nullable = false)
    private String cognome;

    @NonNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(name = "area_geografica")
    private String areaGeografica;

    @Column(name = "genere")
    private String genere;

    @Column(name = "biografia")
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
