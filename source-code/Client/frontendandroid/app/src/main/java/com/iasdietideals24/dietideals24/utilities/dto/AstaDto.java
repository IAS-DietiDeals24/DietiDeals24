package com.iasdietideals24.dietideals24.utilities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iasdietideals24.dietideals24.utilities.data.AnteprimaAsta;
import com.iasdietideals24.dietideals24.utilities.data.Asta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

abstract public class AstaDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Long idAsta = 0L;

    protected String stato = "ACTIVE";

    protected CategoriaAstaShallowDto categoriaShallow = new CategoriaAstaShallowDto();

    protected String nome = "";

    protected String descrizione = "";

    protected LocalDate dataScadenza = LocalDate.now(ZoneOffset.UTC);

    protected LocalTime oraScadenza = LocalTime.now(ZoneOffset.UTC);

    protected byte[] immagine = new byte[0];

    protected Set<NotificaShallowDto> notificheAssociateShallow = new HashSet<>();

    protected AccountShallowDto proprietarioShallow = new AccountShallowDto();

    private Set<OffertaShallowDto> offerteRicevuteShallow = new HashSet<>();

    public AstaDto(Long idAsta, String stato, CategoriaAstaShallowDto categoriaShallow, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, Set<OffertaShallowDto> offerteRicevuteShallow) {
        this.idAsta = idAsta;
        this.stato = stato;
        this.categoriaShallow = categoriaShallow;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.oraScadenza = oraScadenza;
        this.immagine = immagine;
        this.notificheAssociateShallow = notificheAssociateShallow;
        this.proprietarioShallow = proprietarioShallow;
        this.offerteRicevuteShallow = offerteRicevuteShallow;
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

    public String getStato() {
        return this.stato;
    }

    public CategoriaAstaShallowDto getCategoriaShallow() {
        return this.categoriaShallow;
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

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public LocalTime getOraScadenza() {
        return oraScadenza;
    }

    public Set<NotificaShallowDto> getNotificheAssociateShallow() {
        return notificheAssociateShallow;
    }

    public AccountShallowDto getProprietarioShallow() {
        return this.proprietarioShallow;
    }

    public Set<OffertaShallowDto> getOfferteRicevuteShallow() {
        return offerteRicevuteShallow;
    }
}
