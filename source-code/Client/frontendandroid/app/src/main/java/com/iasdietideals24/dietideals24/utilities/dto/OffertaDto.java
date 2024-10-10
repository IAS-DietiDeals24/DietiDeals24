package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Offerta;
import com.iasdietideals24.dietideals24.utilities.data.OffertaRicevuta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class OffertaDto {

    protected Long idOfferta = 0L;

    protected LocalDate dataInvio = LocalDate.now();

    protected LocalTime oraInvio = LocalTime.now();

    protected BigDecimal valore = BigDecimal.ZERO;

    public OffertaDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore) {
        this.idOfferta = idOfferta;
        this.dataInvio = dataInvio;
        this.oraInvio = oraInvio;
        this.valore = valore;
    }

    public OffertaDto() {
    }

    public abstract Offerta toOfferta();

    public abstract OffertaRicevuta toOffertaRicevuta();

    public Long getIdOfferta() {
        return this.idOfferta;
    }

    public void setIdOfferta(Long idOfferta) {
        this.idOfferta = idOfferta;
    }

    public LocalDate getDataInvio() {
        return this.dataInvio;
    }

    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }

    public LocalTime getOraInvio() {
        return this.oraInvio;
    }

    public void setOraInvio(LocalTime oraInvio) {
        this.oraInvio = oraInvio;
    }

    public BigDecimal getValore() {
        return this.valore;
    }

    public void setValore(BigDecimal valore) {
        this.valore = valore;
    }
}
