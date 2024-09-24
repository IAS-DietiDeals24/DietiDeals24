package com.iasdietideals24.dietideals24.utilities.dto.utilities;

import java.time.LocalDate;

public class AnagraficaProfiloDto {

    private String nome;

    private String cognome;

    private LocalDate dataNascita;

    private String areaGeografica;

    private String genere;

    private String biografia;

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

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return this.dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getAreaGeografica() {
        return this.areaGeografica;
    }

    public void setAreaGeografica(String areaGeografica) {
        this.areaGeografica = areaGeografica;
    }

    public String getGenere() {
        return this.genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getBiografia() {
        return this.biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
}
