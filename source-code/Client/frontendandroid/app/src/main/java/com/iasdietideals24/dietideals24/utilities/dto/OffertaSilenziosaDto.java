package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta;
import com.iasdietideals24.dietideals24.utilities.classes.data.OffertaRicevuta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffertaSilenziosaDto extends OffertaDiCompratoreDto {

    private Boolean isAccettata = null;

    private AstaShallowDto astaRiferimentoShallow = new AstaShallowDto();

    public OffertaSilenziosaDto(Long idOfferta, LocalDate dataInvio, LocalTime oraInvio, BigDecimal valore, AccountShallowDto compratoreCollegatoShallow, Boolean isAccettata, AstaShallowDto astaRiferimentoShallow) {
        super(idOfferta, dataInvio, oraInvio, valore, compratoreCollegatoShallow);
        this.isAccettata = isAccettata;
        this.astaRiferimentoShallow = astaRiferimentoShallow;
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
                compratoreCollegatoShallow.getEmail(),
                "",
                new byte[]{},
                valore,
                dataInvio,
                oraInvio,
                isAccettata
        );
    }
}
