package com.iasdietideals24.dietideals24.utilities.dto.utilities;

import java.time.LocalDate;

public class AnagraficaProfiloDto {

    private String nome = "";

    private String cognome = "";

    private LocalDate dataNascita = LocalDate.now();

    private String areaGeografica = "";

    private String genere = "";

    private String biografia = "";

    public AnagraficaProfiloDto(String nome, String cognome, LocalDate dataNascita, String areaGeografica, String genere, String biografia) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.areaGeografica = areaGeografica;
        this.genere = genere;
        this.biografia = biografia;
    }

    public AnagraficaProfiloDto() {
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public LocalDate getDataNascita() {
        return this.dataNascita;
    }

    public String getAreaGeografica() {
        return this.areaGeografica;
    }

    public String getGenere() {
        return this.genere;
    }

    public String getBiografia() {
        return this.biografia;
    }
}
