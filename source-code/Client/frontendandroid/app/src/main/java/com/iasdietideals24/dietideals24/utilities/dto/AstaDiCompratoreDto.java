package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public abstract class AstaDiCompratoreDto extends AstaDto {

    public AstaDiCompratoreDto(Long idAsta, CategoriaAstaShallowDto categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow, Set<OffertaShallowDto> offerteRicevuteShallow) {
        super(idAsta, categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow, proprietarioShallow, offerteRicevuteShallow);
    }

    public AstaDiCompratoreDto() {
    }
}
