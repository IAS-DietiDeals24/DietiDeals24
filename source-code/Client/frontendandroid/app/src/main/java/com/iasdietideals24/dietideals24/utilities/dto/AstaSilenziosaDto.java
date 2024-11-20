package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.AnteprimaAsta;
import com.iasdietideals24.dietideals24.utilities.data.Asta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class AstaSilenziosaDto extends AstaDiVenditoreDto {

    public AstaSilenziosaDto(Long idAsta, CategoriaAstaShallowDto categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, Set<OffertaShallowDto> offerteRicevuteShallow) {
        super(idAsta, categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow, proprietarioShallow, offerteRicevuteShallow);
    }

    public AstaSilenziosaDto() {
    }

    public AnteprimaAsta toAnteprimaAsta() {
        return new AnteprimaAsta(idAsta, TipoAsta.SILENZIOSA, dataScadenza, oraScadenza, immagine,
                nome, new BigDecimal("0.0"));
    }

    public Asta toAsta() {
        return new Asta(idAsta, proprietarioShallow.getEmail(), TipoAsta.SILENZIOSA, dataScadenza, oraScadenza, new BigDecimal("0.0"), immagine, nome, CategoriaAsta.Companion.fromStringToEnum(categoriaShallow.getNome()), descrizione);
    }
}
