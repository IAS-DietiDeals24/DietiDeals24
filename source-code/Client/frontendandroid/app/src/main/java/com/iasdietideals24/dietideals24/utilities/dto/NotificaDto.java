package com.iasdietideals24.dietideals24.utilities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iasdietideals24.dietideals24.utilities.data.Notifica;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto;
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class NotificaDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idNotifica = 0L;

    private LocalDate dataInvio = LocalDate.now(ZoneOffset.UTC);

    private LocalTime oraInvio = LocalTime.now(ZoneOffset.UTC);

    private String messaggio = "";

    private AccountShallowDto mittenteShallow = new AccountShallowDto();

    private Set<AccountShallowDto> destinatariShallow = new HashSet<>();

    private AstaShallowDto astaAssociataShallow = new AstaShallowDto();

    public NotificaDto() {
    }

    public Notifica toNotifica() {
        ZonedDateTime utc = ZonedDateTime.of(this.dataInvio, this.oraInvio, ZoneOffset.UTC);
        ZonedDateTime local = utc.withZoneSameInstant(ZoneId.systemDefault());
        LocalDate dataInvio = local.toLocalDate();
        LocalTime oraInvio = local.toLocalTime();

        return new Notifica(
                astaAssociataShallow.getIdAsta(),
                TipoAsta.valueOf(astaAssociataShallow.getTipoAstaSpecifica()),
                mittenteShallow.getIdAccount(),
                "",
                new byte[]{},
                messaggio,
                dataInvio,
                oraInvio
        );
    }

    public Long getIdNotifica() {
        return this.idNotifica;
    }

    public LocalDate getDataInvio() {
        return this.dataInvio;
    }

    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }

    public LocalTime getOraInvio() {
        return this.oraInvio;
    }

    public void setOraInvio(LocalTime oraInvio) {
        this.oraInvio = oraInvio;
    }

    public String getMessaggio() {
        return this.messaggio;
    }

    public AccountShallowDto getMittenteShallow() {
        return this.mittenteShallow;
    }

    public Set<AccountShallowDto> getDestinatariShallow() {
        return this.destinatariShallow;
    }

    public AstaShallowDto getAstaAssociataShallow() {
        return this.astaAssociataShallow;
    }
}
