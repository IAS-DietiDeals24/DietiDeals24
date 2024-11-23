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

public class AstaInversaDto extends AstaDiCompratoreDto {

    private BigDecimal sogliaIniziale = BigDecimal.ZERO;

    public AstaInversaDto(Long idAsta, CategoriaAstaShallowDto categoriaShallow, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, Set<OffertaShallowDto> offerteRicevuteShallow, BigDecimal sogliaIniziale) {
        super(idAsta, categoriaShallow, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow, proprietarioShallow, offerteRicevuteShallow);
        this.sogliaIniziale = sogliaIniziale;
    }

    public AstaInversaDto() {
    }

    public AnteprimaAsta toAnteprimaAsta() {
        return new AnteprimaAsta(idAsta, TipoAsta.INVERSA, dataScadenza, oraScadenza, immagine,
                nome, new BigDecimal("0.0"));
    }

    public Asta toAsta() {
        return new Asta(idAsta, proprietarioShallow.getEmail(), TipoAsta.INVERSA, dataScadenza, oraScadenza, sogliaIniziale, immagine, nome, CategoriaAsta.Companion.fromStringToEnum(categoriaShallow.getNome()), descrizione);
    }

    public BigDecimal getSogliaIniziale() {
        return this.sogliaIniziale;
    }
}
