package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.AnteprimaAsta;
import com.iasdietideals24.dietideals24.utilities.data.Asta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

abstract public class AstaDto {
    protected Long idAsta = 0L;

    protected CategoriaAstaShallowDto categoriaShallow = new CategoriaAstaShallowDto();

    protected String nome = "";

    protected String descrizione = "";

    protected LocalDate dataScadenza = LocalDate.now();

    protected LocalTime oraScadenza = LocalTime.now();

    protected byte[] immagine = new byte[0];

    protected Set<NotificaShallowDto> notificheAssociateShallow = new HashSet<>();

    public AstaDto(Long idAsta, CategoriaAstaShallowDto categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow) {
        this.idAsta = idAsta;
        this.categoriaShallow = categoria;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.oraScadenza = oraScadenza;
        this.immagine = immagine;
        this.notificheAssociateShallow = notificheAssociateShallow;
    }

    public AstaDto() {
    }

    public abstract Asta toAsta();

    public abstract AnteprimaAsta toAnteprimaAsta();

    public Long getIdAsta() {
        return this.idAsta;
    }

    public void setIdAsta(Long idAsta) {
        this.idAsta = idAsta;
    }

    public CategoriaAstaShallowDto getCategoria() {
        return this.categoriaShallow;
    }

    public void setCategoria(CategoriaAstaShallowDto categoria) {
        this.categoriaShallow = categoria;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getImmagine() {
        return this.immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

}
