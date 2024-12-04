package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Offerta;
import com.iasdietideals24.dietideals24.utilities.data.OffertaRicevuta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoOfferta;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class OffertaSilenziosaDto extends OffertaDiCompratoreDto {

    private String stato = "";

    public OffertaSilenziosaDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto compratoreCollegatoShallow, String stato, AstaShallowDto astaRiferimentoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore, compratoreCollegatoShallow, astaRiferimentoShallow);
        this.stato = stato;
    }

    public OffertaSilenziosaDto() {
    }

    public Offerta toOfferta() {
        ZonedDateTime utc = ZonedDateTime.of(this.dataInvio, this.oraInvio, ZoneOffset.UTC);
        ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault());
        LocalDate dataInvio = local.toLocalDate();
        LocalTime oraInvio = local.toLocalTime();

        return new Offerta(
                idOfferta,
                astaRiferimentoShallow.getIdAsta(),
                compratoreCollegatoShallow.getIdAccount(),
                valore,
                dataInvio,
                oraInvio
        );
    }

    public OffertaRicevuta toOffertaRicevuta() {
        ZonedDateTime utc = ZonedDateTime.of(this.dataInvio, this.oraInvio, ZoneOffset.UTC);
        ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault());
        LocalDate dataInvio = local.toLocalDate();
        LocalTime oraInvio = local.toLocalTime();

        return new OffertaRicevuta(
                idOfferta,
                astaRiferimentoShallow.getIdAsta(),
                TipoAsta.valueOf(astaRiferimentoShallow.getTipoAstaSpecifica()),
                compratoreCollegatoShallow.getIdAccount(),
                "",
                new byte[]{},
                valore,
                dataInvio,
                oraInvio,
                StatoOfferta.valueOf(stato)
        );
    }

    public String getStato() {
        return this.stato;
    }
}
