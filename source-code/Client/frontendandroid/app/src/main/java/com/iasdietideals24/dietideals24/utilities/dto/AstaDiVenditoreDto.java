package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public abstract class AstaDiVenditoreDto extends AstaDto {

    protected AccountShallowDto proprietarioShallow = new AccountShallowDto();

    public AstaDiVenditoreDto(Long idAsta, String categoria, String nome, String descrizione, LocalDate dataScadenza, LocalTime oraScadenza, byte[] immagine, Set<NotificaShallowDto> notificheAssociateShallow, AccountShallowDto proprietarioShallow) {
        super(idAsta, categoria, nome, descrizione, dataScadenza, oraScadenza, immagine, notificheAssociateShallow);
        this.proprietarioShallow = proprietarioShallow;
    }

    public AstaDiVenditoreDto() {
    }
}
