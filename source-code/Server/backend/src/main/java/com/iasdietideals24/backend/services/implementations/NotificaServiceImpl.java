package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.NotificaMapper;
import com.iasdietideals24.backend.repositories.NotificaRepository;
import com.iasdietideals24.backend.services.NotificaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        // Verifichiamo l'integrità dei dati
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
        Page<Notifica> foundNotifiche = notificaRepository.findAll(pageable);

        return foundNotifiche.map(notificaMapper::toDto);
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

        updatedNotificaDto.setIdNotifica(idNotifica);

        if (!notificaRepository.existsById(idNotifica))
            throw new UpdateRuntimeException("L'id notifica \"" + idNotifica + "\" non corrisponde a nessuna notifica esistente!");
        else {
            // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
            return this.create(updatedNotificaDto);
        }
    }

    @Override
    public NotificaDto partialUpdate(Long idNotifica, NotificaDto updatedNotificaDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedNotificaDto.setIdNotifica(idNotifica);

        Optional<Notifica> foundNotifica = notificaRepository.findById(idNotifica);
        if (foundNotifica.isEmpty())
            throw new UpdateRuntimeException("L'id notifica \"" + idNotifica + "\" non corrisponde a nessuna notifica esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            Notifica existingNotifica = foundNotifica.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedNotificaDto, existingNotifica);

            return notificaMapper.toDto(notificaRepository.save(existingNotifica));
        }
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

    private void checkDataInvioValid(LocalDate dataInvio) throws InvalidParameterException {
        if (dataInvio == null)
            throw new InvalidParameterException("La data di invio non può essere null!");
        else if (dataInvio.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di invio non può essere successiva alla data odierna!");
    }

    private void checkOraInvioValid(LocalTime oraInvio) throws InvalidParameterException {
        if (oraInvio == null)
            throw new InvalidParameterException("L'ora di invio non può essere null!");
        else if (oraInvio.isAfter(LocalTime.now()))
            throw new InvalidParameterException("L'ora di invio non può essere successiva all'ora odierna!");
    }

    private void checkMessaggioValid(String messaggio) throws InvalidParameterException {
        if (messaggio == null)
            throw new InvalidParameterException("Il messaggio non può essere null!");
        else if (messaggio.isBlank())
            throw new InvalidParameterException("Il messaggio non può essere vuoto!");
    }

    private void checkDestinatariValid(Set<AccountShallowDto> accountsShallowDto) throws InvalidParameterException {
        if (accountsShallowDto == null)
            throw new InvalidParameterException("La lista di destinatari non può essere null!");

        for (AccountShallowDto destinatarioDto : accountsShallowDto) {
            checkMittenteDestinatarioValid(destinatarioDto);
        }
    }

    private void checkMittenteDestinatarioValid(AccountShallowDto accountShallowDto) throws InvalidParameterException {
        if (accountShallowDto == null)
            throw new InvalidParameterException("L'account mittente/destinatario non può essere null!");

        checkEmailValid(accountShallowDto.getEmail());
    }

    private void checkEmailValid(String email) throws InvalidParameterException {
        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");
    }

    private void checkAstaAssociataShallow(AstaShallowDto astaAssociataShallow) throws InvalidParameterException {
        if (astaAssociataShallow == null)
            throw new InvalidParameterException("L'asta associata non può essere null!");
    }

    @Override
    public void convertRelations(NotificaDto notificaDto, Notifica notifica) throws InvalidParameterException {
        convertMittenteShallow(notificaDto.getMittenteShallow(), notifica);
        convertDestinatariShallow(notificaDto.getDestinatariShallow(), notifica);
        convertAstaAssociataShallow(notificaDto.getAstaAssociataShallow(), notifica);
    }

    private void convertMittenteShallow(AccountShallowDto mittenteShallowDto, Notifica notifica) throws InvalidParameterException {
        Account convertedAccount = relationsConverter.convertAccountShallowRelation(mittenteShallowDto);

        if (convertedAccount != null) {
            notifica.setMittente(convertedAccount);
            convertedAccount.addNotificaInviata(notifica);
        }
    }

    private void convertDestinatariShallow(Set<AccountShallowDto> destinatariShallowDto, Notifica notifica) throws InvalidParameterException {
        if (destinatariShallowDto != null) {
            for (AccountShallowDto accountShallowDto : destinatariShallowDto) {

                Account convertedAccount = relationsConverter.convertAccountShallowRelation(accountShallowDto);

                if (convertedAccount != null) {
                    notifica.addDestinatario(convertedAccount);
                    convertedAccount.addNotificaRicevuta(notifica);
                }
            }
        }
    }

    private void convertAstaAssociataShallow(AstaShallowDto astaAssociataShallowDto, Notifica notifica) throws InvalidParameterException {
        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaAssociataShallowDto);

        if (convertedAsta != null) {
            notifica.setAstaAssociata(convertedAsta);
            convertedAsta.addNotificaAssociata(notifica);
        }
    }

    @Override
    public void updatePresentFields(NotificaDto updatedNotificaDto, Notifica existingNotifica) throws InvalidParameterException {
        ifPresentUpdatDataInvio(updatedNotificaDto.getDataInvio(), existingNotifica);
        ifPresentUpdateOraInvio(updatedNotificaDto.getOraInvio(), existingNotifica);
        ifPresentUpdateMessaggio(updatedNotificaDto.getMessaggio(), existingNotifica);

        // Non è possibile modificare le associazioni "mittente", "destinatari", "astaAssociata" tramite la risorsa "notifiche"
    }

    private void ifPresentUpdatDataInvio(LocalDate updatedDataInvio, Notifica existingNotifica) throws InvalidParameterException {
        if (updatedDataInvio != null) {
            this.checkDataInvioValid(updatedDataInvio);
            existingNotifica.setDataInvio(updatedDataInvio);
        }
    }

    private void ifPresentUpdateOraInvio(LocalTime updatedOraInvio, Notifica existingNotifica) throws InvalidParameterException {
        if (updatedOraInvio != null) {
            this.checkOraInvioValid(updatedOraInvio);
            existingNotifica.setOraInvio(updatedOraInvio);
        }
    }

    private void ifPresentUpdateMessaggio(String updatedMessaggio, Notifica existingNotifica) throws InvalidParameterException {
        if (updatedMessaggio != null) {
            this.checkMessaggioValid(updatedMessaggio);
            existingNotifica.setMessaggio(updatedMessaggio);
        }
    }
}
