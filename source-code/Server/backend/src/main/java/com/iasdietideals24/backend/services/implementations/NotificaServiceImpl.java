package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CompratoreMapper;
import com.iasdietideals24.backend.mapstruct.mappers.NotificaMapper;
import com.iasdietideals24.backend.mapstruct.mappers.VenditoreMapper;
import com.iasdietideals24.backend.repositories.CompratoreRepository;
import com.iasdietideals24.backend.repositories.NotificaRepository;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.NotificaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Service
public class NotificaServiceImpl implements NotificaService {

    private final NotificaMapper notificaMapper;
    private final NotificaRepository notificaRepository;
    private final RelationsConverter relationsConverter;

    public NotificaServiceImpl(NotificaMapper notificaMapper,
                               NotificaRepository notificaRepository,
                               RelationsConverter relationsConverter) {
        this.notificaMapper = notificaMapper;
        this.notificaRepository = notificaRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public NotificaDto create(NotificaDto nuovaNotificaDto) throws InvalidParameterException {

        //Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaNotificaDto);

        // Convertiamo a entità
        Notifica nuovaNotifica = notificaMapper.toEntity(nuovaNotificaDto);

        // Recuperiamo le associazioni
        convertRelations(nuovaNotificaDto, nuovaNotifica);

        // Registriamo l'entità
        Notifica savedNotifica = notificaRepository.save(nuovaNotifica);

        return notificaMapper.toDto(savedNotifica);
    }

    @Override
    public Page<NotificaDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<Notifica> foundNotifica = notificaRepository.findAll(pageable);

        return foundNotifica.map(notificaMapper::toDto);
    }

    @Override
    public Optional<NotificaDto> findOne(Long idNotifica) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Notifica> foundNotifica = notificaRepository.findById(idNotifica);

        return foundNotifica.map(notificaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idNotifica) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return notificaRepository.existsById(idNotifica);
    }

    @Override
    public NotificaDto fullUpdate(Long idNotifica, NotificaDto updatedNotificaDto) throws InvalidParameterException {

        if (!notificaRepository.existsById(idNotifica))
            throw new UpdateRuntimeException("L'id notifica \"" + idNotifica + "\" non corrisponde a nessuna notifica!");

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(updatedNotificaDto);
    }

    @Override
    public NotificaDto partialUpdate(Long idNotifica, NotificaDto updatedNotificaDto) {
        //TODO
        return null;
    }

    @Override
    public void delete(Long idNotifica) {

        // Eliminiamo l'entità con l'id passato per parametro
        notificaRepository.deleteById(idNotifica);
    }

    @Override
    public void checkFieldsValid(NotificaDto notificaDto) throws InvalidParameterException {
        checkDataInvioValid(notificaDto.getDataInvio());
        checkOraInvioValid(notificaDto.getOraInvio());
        checkMessaggioValid(notificaDto.getMessaggio());
        checkMittenteDestinatarioValid(notificaDto.getMittenteShallow());
        checkDestinatariValid(notificaDto.getDestinatariShallow());
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

    void checkDestinatariValid(Set<AccountShallowDto> accountsShallowDto) throws InvalidParameterException {
        if (accountsShallowDto == null)
            throw new InvalidParameterException("La lista di destinatari non può essere null!");

        for (AccountShallowDto destinatarioDto : accountsShallowDto) {
            checkMittenteDestinatarioValid(destinatarioDto);
        }
    }

    void checkMittenteDestinatarioValid(AccountShallowDto accountShallowDto) throws InvalidParameterException {
        if (accountShallowDto == null)
            throw new InvalidParameterException("L'account mittente/destinatario non può essere null!");

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

    void checkAstaAssociataShallow(AstaShallowDto astaAssociataShallow) throws InvalidParameterException {
        if (astaAssociataShallow == null)
            throw new InvalidParameterException("L'asta associata non può essere null!");
    }

    @Override
    public void convertRelations(NotificaDto notificaDto, Notifica notifica) throws InvalidParameterException {
        convertMittenteShallow(notificaDto.getMittenteShallow(), notifica);
        convertDestinatariShallow(notificaDto.getDestinatariShallow(), notifica);
        convertAstaAssociataShallow(notificaDto.getAstaAssociataShallow(), notifica);
    }

    void convertMittenteShallow(AccountShallowDto mittenteShallowDto, Notifica nuovaNotifica) throws InvalidParameterException {
        Account convertedAccount = relationsConverter.convertAccountShallowRelation(mittenteShallowDto);

        if (convertedAccount != null) {
            nuovaNotifica.setMittente(convertedAccount);
            convertedAccount.addNotificaInviata(nuovaNotifica);
        }
    }

    void convertDestinatariShallow(Set<AccountShallowDto> destinatariShallowDto, Notifica nuovaNotifica) throws InvalidParameterException {
        if (destinatariShallowDto != null) {
            for (AccountShallowDto accountShallowDto : destinatariShallowDto) {

                Account convertedAccount = relationsConverter.convertAccountShallowRelation(accountShallowDto);

                if (convertedAccount != null) {
                    nuovaNotifica.addDestinatario(convertedAccount);
                    convertedAccount.addNotificaRicevuta(nuovaNotifica);
                }
            }
        }
    }

    void convertAstaAssociataShallow(AstaShallowDto astaAssociataShallowDto, Notifica nuovaNotifica) throws InvalidParameterException {
        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaAssociataShallowDto);

        if (convertedAsta != null) {
            nuovaNotifica.setAstaAssociata(convertedAsta);
            convertedAsta.addNotificaAssociata(nuovaNotifica);
        }
    }

    @Override
    public void updatePresentFields(NotificaDto updatedNotificaDto, Notifica existingNotifica) throws InvalidParameterException {
        //TODO
    }
}
