package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta;
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificaDto {

    private Long idNotifica = 0L;

    private LocalDate dataInvio = LocalDate.now();

    private LocalTime oraInvio = LocalTime.now();

    private String messaggio = "";

    private AccountShallowDto mittenteShallow = new AccountShallowDto();

    private Set<AccountShallowDto> destinatariShallow = new HashSet<>();

    private AstaShallowDto astaAssociataShallow = new AstaShallowDto();

    public Notifica toNotifica() {
        return new Notifica(astaAssociataShallow.getIdAsta(), TipoAsta.valueOf(astaAssociataShallow.getTipoAstaSpecifica()),
                mittenteShallow.getEmail(), "", new byte[]{}, messaggio, dataInvio, oraInvio);
    }
}
