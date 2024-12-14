package com.iasdietideals24.dietideals24.utilities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iasdietideals24.dietideals24.utilities.data.Offerta;
import com.iasdietideals24.dietideals24.utilities.data.OffertaRicevuta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Objects;

public abstract class OffertaDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Long idOfferta = 0L;

    protected LocalDate dataInvio = LocalDate.now(ZoneOffset.UTC);

    protected LocalTime oraInvio = LocalTime.now(ZoneOffset.UTC);

    protected BigDecimal valore = new BigDecimal("0.00");

    protected AstaShallowDto astaRiferimentoShallow = new AstaShallowDto();

    public OffertaDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AstaShallowDto astaRiferimentoShallow) {
        this.idOfferta = idOfferta;
        this.dataInvio = dataInvio;
        this.oraInvio = oraInvio;
        this.valore = valore;
        this.astaRiferimentoShallow = astaRiferimentoShallow;
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

    public AstaShallowDto getAstaRiferimentoShallow() {
        return this.astaRiferimentoShallow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffertaDto that = (OffertaDto) o;
        return Objects.equals(idOfferta, that.idOfferta) && Objects.equals(dataInvio, that.dataInvio) && Objects.equals(oraInvio, that.oraInvio) && Objects.equals(valore, that.valore) && Objects.equals(astaRiferimentoShallow, that.astaRiferimentoShallow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOfferta, dataInvio, oraInvio, valore, astaRiferimentoShallow);
    }
}
