package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CompratoreMapper;
import com.iasdietideals24.backend.mapstruct.mappers.VenditoreMapper;
import com.iasdietideals24.backend.repositories.CompratoreRepository;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
import com.iasdietideals24.backend.services.NotificaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Service
public class NotificaServiceImpl implements NotificaService {

    private final CompratoreMapper compratoreMapper;
    private final VenditoreMapper venditoreMapper;

    private final CompratoreRepository compratoreRepository;
    private final VenditoreRepository venditoreRepository;

    public NotificaServiceImpl(CompratoreMapper compratoreMapper,
                               VenditoreMapper venditoreMapper,
                               CompratoreRepository compratoreRepository,
                               VenditoreRepository venditoreRepository) {

        this.compratoreMapper = compratoreMapper;
        this.venditoreMapper = venditoreMapper;

        this.compratoreRepository = compratoreRepository;
        this.venditoreRepository = venditoreRepository;
    }

    @Override
    public NotificaDto create(NotificaDto nuovaNotificaDto) throws InvalidParameterException {

        //Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaNotificaDto);
        return null;
    }

    @Override
    public Page<NotificaDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<NotificaDto> findOne(Long idNotifica) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(Long idNotifica) {
        return false;
    }

    @Override
    public NotificaDto fullUpdate(Long idNotifica, NotificaDto updatedNotificaDto) {
        return null;
    }

    @Override
    public NotificaDto partialUpdate(Long idNotifica, NotificaDto updatedNotificaDto) {
        return null;
    }

    @Override
    public void delete(Long idNotifica) throws InvalidParameterException {

    }

    @Override
    public void checkFieldsValid(NotificaDto notificaDto) throws InvalidParameterException {
        checkDataInvioValid(notificaDto.getDataInvio());
        checkOraInvioValid(notificaDto.getOraInvio());
        checkMessaggioValid(notificaDto.getMessaggio());
        checkAccountShallowValid(notificaDto.getMittenteShallow());
        checkAccountShallowValid(notificaDto.getDestinatariShallow());
        checkAstaAssociataShallow(notificaDto.getAstaAssociataShallow());
    }

    void checkDataInvioValid(LocalDate dataInvio) throws InvalidParameterException {
        if (dataInvio == null)
            throw new InvalidParameterException("La data di invio non può essere null!");
        else if (dataInvio.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di invio non può essere successiva alla data odierna!");
    }

    void checkOraInvioValid(LocalTime oraInvio) throws InvalidParameterException {
        if (oraInvio == null)
            throw new InvalidParameterException("L'ora di invio non può essere null!");
        else if (oraInvio.isAfter(LocalTime.now()))
            throw new InvalidParameterException("L'ora di invio non può essere successiva alla data odierna!");
    }

    void checkMessaggioValid(String messaggio) throws InvalidParameterException {
        if (messaggio == null)
            throw new InvalidParameterException("Il messaggio non può essere null!");
        else if (messaggio.isBlank())
            throw new InvalidParameterException("Il messaggio non può essere vuoto!");
    }

    void checkAccountShallowValid(AccountShallowDto accountShallowDto) throws InvalidParameterException {
        if (accountShallowDto == null)
            throw new InvalidParameterException("L'account non può essere null!");

        checkEmailValid(accountShallowDto.getEmail());
    }

    void checkEmailValid(String email) throws InvalidParameterException {
        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");
    }

    void checkAccountShallowValid(Set<AccountShallowDto> accountsShallowDto) throws InvalidParameterException {
        if (accountsShallowDto == null)
            throw new InvalidParameterException("La lista di account non può essere null!");

        for (AccountShallowDto destinatarioDto : accountsShallowDto) {
            checkAccountShallowValid(destinatarioDto);
        }
    }

    void checkAstaAssociataShallow(AstaShallowDto astaAssociataShallow) throws InvalidParameterException {
        if (astaAssociataShallow == null)
            throw new InvalidParameterException("L'asta associata non può essere null!");
    }

    @Override
    public void convertRelations(NotificaDto notificaDto, Notifica notifica) throws InvalidParameterException {

    }

    @Override
    public void updatePresentFields(NotificaDto updatedNotificaDto, Notifica existingNotifica) throws InvalidParameterException {

    }
}
