package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public abstract class AstaDiVenditoreDto extends AstaDto {

    public AstaDiVenditoreDto(Long idAsta, CategoriaAstaShallowDto categoriaShallow, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, Set<OffertaShallowDto> offerteRicevuteShallow) {
        super(idAsta, categoriaShallow, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow, proprietarioShallow, offerteRicevuteShallow);
    }

    public AstaDiVenditoreDto() {
    }
}
