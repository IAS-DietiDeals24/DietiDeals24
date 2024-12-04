package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Offerta;
import com.iasdietideals24.dietideals24.utilities.data.OffertaRicevuta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class OffertaInversaDto extends OffertaDiVenditoreDto {

    public OffertaInversaDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto venditoreCollegatoShallow, AstaShallowDto astaRiferimentoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore, venditoreCollegatoShallow, astaRiferimentoShallow);
    }

    public OffertaInversaDto() {
    }

    public Offerta toOfferta() {
        ZonedDateTime utc = ZonedDateTime.of(this.dataInvio, this.oraInvio, ZoneOffset.UTC);
        ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault());
        LocalDate dataInvio = local.toLocalDate();
        LocalTime oraInvio = local.toLocalTime();

        return new Offerta(
                idOfferta,
                astaRiferimentoShallow.getIdAsta(),
                venditoreCollegatoShallow.getIdAccount(),
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
                TipoAsta.Companion.getEnum(astaRiferimentoShallow.getTipoAstaSpecifica()),
                venditoreCollegatoShallow.getIdAccount(),
                "",
                new byte[]{},
                valore,
                dataInvio,
                oraInvio,
                null
        );
    }
}
