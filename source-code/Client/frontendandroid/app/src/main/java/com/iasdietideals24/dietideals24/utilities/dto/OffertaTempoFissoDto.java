package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.Offerta;
import com.iasdietideals24.dietideals24.utilities.data.OffertaRicevuta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class OffertaTempoFissoDto extends OffertaDiCompratoreDto {

    public OffertaTempoFissoDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto compratoreCollegatoShallow, AstaShallowDto astaRiferimentoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore, compratoreCollegatoShallow, astaRiferimentoShallow);
    }

    public OffertaTempoFissoDto() {
    }

    public Offerta toOfferta() {
        return new Offerta(
                idOfferta,
                astaRiferimentoShallow.getIdAsta(),
                compratoreCollegatoShallow.getEmail(),
                valore,
                dataInvio,
                oraInvio
        );
    }

    public OffertaRicevuta toOffertaRicevuta() {
        return new OffertaRicevuta(
                idOfferta,
                astaRiferimentoShallow.getIdAsta(),
                TipoAsta.valueOf(astaRiferimentoShallow.getTipoAstaSpecifica()),
                compratoreCollegatoShallow.getEmail(),
                "",
                new byte[]{},
                valore,
                dataInvio,
                oraInvio,
                null
        );
    }
}
