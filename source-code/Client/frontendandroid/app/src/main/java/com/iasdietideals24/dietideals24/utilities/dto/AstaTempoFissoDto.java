package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta;
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta;
import com.iasdietideals24.dietideals24.utilities.classes.data.Asta;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AstaTempoFissoDto extends AstaDiVenditoreDto {

    private BigDecimal sogliaMinima = BigDecimal.ZERO;

    public AstaTempoFissoDto(Long idAsta, String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, BigDecimal sogliaMinima) {
        super(idAsta, categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow, proprietarioShallow);
        this.sogliaMinima = sogliaMinima;
    }

    public AnteprimaAsta toAnteprimaAsta() {
        return new AnteprimaAsta(idAsta, TipoAsta.TEMPO_FISSO, dataScadenza, oraScadenza, immagine,
                nome, new BigDecimal("0.0"));
    }

    public Asta toAsta() {
        return new Asta(idAsta, proprietarioShallow.getEmail(), TipoAsta.INVERSA, dataScadenza, oraScadenza, sogliaMinima, immagine, nome, categoria, descrizione);
    }
}
