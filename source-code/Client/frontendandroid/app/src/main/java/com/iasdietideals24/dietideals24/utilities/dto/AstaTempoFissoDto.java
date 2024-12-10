package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.data.AnteprimaAsta;
import com.iasdietideals24.dietideals24.utilities.data.Asta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta;
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoAsta;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class AstaTempoFissoDto extends AstaDiVenditoreDto {

    private BigDecimal sogliaMinima = new BigDecimal("0.00");

    public AstaTempoFissoDto(Long idAsta, String stato, CategoriaAstaShallowDto categoriaShallow, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, Set<OffertaShallowDto> offerteRicevuteShallow, BigDecimal sogliaMinima) {
        super(idAsta, stato, categoriaShallow, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow, proprietarioShallow, offerteRicevuteShallow);
        this.sogliaMinima = sogliaMinima;
    }

    public AstaTempoFissoDto() {
    }

    public AnteprimaAsta toAnteprimaAsta() {
        ZonedDateTime utc = ZonedDateTime.of(this.dataScadenza, this.oraScadenza, ZoneOffset.UTC);
        ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);
        LocalDate dataScadenza = local.toLocalDate();
        LocalTime oraScadenza = local.toLocalTime();

        return new AnteprimaAsta(
                idAsta,
                StatoAsta.valueOf(stato),
                TipoAsta.TEMPO_FISSO,
                dataScadenza,
                oraScadenza,
                immagine,
                nome,
                sogliaMinima
        );
    }

    public Asta toAsta() {
        ZonedDateTime utc = ZonedDateTime.of(this.dataScadenza, this.oraScadenza, ZoneOffset.UTC);
        ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);
        LocalDate dataScadenza = local.toLocalDate();
        LocalTime oraScadenza = local.toLocalTime();

        return new Asta(
                idAsta,
                StatoAsta.valueOf(stato),
                proprietarioShallow.getIdAccount(),
                TipoAsta.TEMPO_FISSO,
                dataScadenza,
                oraScadenza, sogliaMinima,
                immagine,
                nome,
                CategoriaAsta.Companion.fromStringToEnum(categoriaShallow.getNome()),
                descrizione
        );
    }

    public BigDecimal getSogliaMinima() {
        return this.sogliaMinima;
    }
}
