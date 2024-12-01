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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class NotificaServiceImpl implements NotificaService {

    public static final String LOG_RECUPERO_NOTIFICA = "Recupero la notifica dal database...";
    public static final String LOG_FOUND_NOTIFICA = "foundNotifica: {}";
    public static final String LOG_NOTIFICA_RECUPERATA = "Notifica recuperata dal database.";

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

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        Notifica nuovaNotifica = notificaMapper.toEntity(nuovaNotificaDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaNotifica: {}", nuovaNotifica);

        // Recuperiamo le associazioni
        convertRelations(nuovaNotificaDto, nuovaNotifica);

        log.trace("nuovaNotifica: {}", nuovaNotifica);

        log.debug("Salvo la notifica nel database...");

        // Registriamo l'entità
        Notifica savedNotifica = notificaRepository.save(nuovaNotifica);

        log.trace("savedNotifica: {}", savedNotifica);
        log.debug("Notifica salvata correttamente nel database con id notifica {}...", savedNotifica.getIdNotifica());

        return notificaMapper.toDto(savedNotifica);
    }

    @Override
    public Page<NotificaDto> findAll(Pageable pageable) {

        log.debug("Recupero le notifiche dal database...");

        // Recuperiamo tutte le entità
        Page<Notifica> foundNotifiche = notificaRepository.findAll(pageable);

        log.trace("foundNotifiche: {}", foundNotifiche);
        log.debug("Notifiche recuperate dal database.");

        return foundNotifiche.map(notificaMapper::toDto);
    }

    @Override
    public Optional<NotificaDto> findOne(Long idNotifica) {

        log.trace("Id notifica da recuperare: {}", idNotifica);
        log.debug(LOG_RECUPERO_NOTIFICA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Notifica> foundNotifica = notificaRepository.findById(idNotifica);

        log.trace(LOG_FOUND_NOTIFICA, foundNotifica);
        log.debug(LOG_NOTIFICA_RECUPERATA);

        return foundNotifica.map(notificaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idNotifica) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return notificaRepository.existsById(idNotifica);
    }

    @Override
    public NotificaDto fullUpdate(Long idNotifica, NotificaDto updatedNotificaDto) throws InvalidParameterException {

        log.trace("Id notifica da sostituire: {}", idNotifica);

        updatedNotificaDto.setIdNotifica(idNotifica);

        if (!notificaRepository.existsById(idNotifica))
            throw new UpdateRuntimeException("L'id notifica '" + idNotifica + "' non corrisponde a nessuna notifica esistente!");
        else {
            // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
            return this.create(updatedNotificaDto);
        }
    }

    @Override
    public NotificaDto partialUpdate(Long idNotifica, NotificaDto updatedNotificaDto) throws InvalidParameterException {

        log.trace("Id notifica da aggiornare: {}", idNotifica);

        updatedNotificaDto.setIdNotifica(idNotifica);

        log.debug(LOG_RECUPERO_NOTIFICA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Notifica> foundNotifica = notificaRepository.findById(idNotifica);

        log.trace(LOG_FOUND_NOTIFICA, foundNotifica);
        log.debug(LOG_NOTIFICA_RECUPERATA);

        if (foundNotifica.isEmpty())
            throw new UpdateRuntimeException("L'id notifica '" + idNotifica + "' non corrisponde a nessuna notifica esistente!");
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

        log.trace("Id notifica da eliminare: {}", idNotifica);
        log.debug("Elimino la notifica dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        notificaRepository.deleteById(idNotifica);

        log.debug("Notifica eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(NotificaDto notificaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di notifica...");

        checkDataInvioValid(notificaDto.getDataInvio());
        checkOraInvioValid(notificaDto.getDataInvio(), notificaDto.getOraInvio());
        checkMessaggioValid(notificaDto.getMessaggio());
        checkMittenteDestinatarioValid(notificaDto.getMittenteShallow());
        checkDestinatariValid(notificaDto.getDestinatariShallow());
        checkAstaAssociataShallow(notificaDto.getAstaAssociataShallow());

        log.debug("Integrità dei dati di notifica verificata.");
    }

    private void checkDataInvioValid(LocalDate dataInvio) throws InvalidParameterException {

        log.trace("Controllo che 'dataInvio' sia valido...");

        if (dataInvio == null)
            throw new InvalidParameterException("La data di invio non può essere null!");
        else if (dataInvio.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di invio non può essere successiva alla data odierna!");

        log.trace("'dataInvio' valido.");
    }

    private void checkOraInvioValid(LocalDate dataInvio, LocalTime oraInvio) throws InvalidParameterException {

        log.trace("Controllo che 'oraInvio' sia valido...");

        if (oraInvio == null)
            throw new InvalidParameterException("L'ora di invio non può essere null!");
        else if (dataInvio.isEqual(LocalDate.now()) && oraInvio.isAfter(LocalTime.now()))
            throw new InvalidParameterException("L'ora di invio non può essere successiva all'ora attuale!");

        log.trace("'oraInvio' valido.");
    }

    private void checkMessaggioValid(String messaggio) throws InvalidParameterException {

        log.trace("Controllo che 'messaggio' sia valido...");

        if (messaggio == null)
            throw new InvalidParameterException("Il messaggio non può essere null!");
        else if (messaggio.isBlank())
            throw new InvalidParameterException("Il messaggio non può essere vuoto!");

        log.trace("'messaggio' valido.");
    }

    private void checkDestinatariValid(Set<AccountShallowDto> accountsShallowDto) throws InvalidParameterException {

        log.trace("Controllo che 'destinatari' sia valido...");

        if (accountsShallowDto == null)
            throw new InvalidParameterException("La lista di destinatari non può essere null!");

        for (AccountShallowDto destinatarioDto : accountsShallowDto) {
            checkMittenteDestinatarioValid(destinatarioDto);
        }

        log.trace("'destinatari' valido.");
    }

    private void checkMittenteDestinatarioValid(AccountShallowDto accountShallowDto) throws InvalidParameterException {

        log.trace("Controllo che 'mittente' sia valido...");

        if (accountShallowDto == null)
            throw new InvalidParameterException("L'account mittente/destinatario non può essere null!");

        log.trace("'mittente' valido.");
    }

    private void checkAstaAssociataShallow(AstaShallowDto astaAssociataShallow) throws InvalidParameterException {

        log.trace("Controllo che 'astaAssociata' sia valido...");

        if (astaAssociataShallow == null)
            throw new InvalidParameterException("L'asta associata non può essere null!");

        log.trace("'astaAssociata' valido.");
    }

    @Override
    public void convertRelations(NotificaDto notificaDto, Notifica notifica) throws InvalidParameterException {

        log.debug("Recupero le associazioni di notifica...");

        convertMittenteShallow(notificaDto.getMittenteShallow(), notifica);
        convertDestinatariShallow(notificaDto.getDestinatariShallow(), notifica);
        convertAstaAssociataShallow(notificaDto.getAstaAssociataShallow(), notifica);

        log.debug("Associazioni di notifica recuperate.");
    }

    private void convertMittenteShallow(AccountShallowDto mittenteShallowDto, Notifica notifica) throws InvalidParameterException {

        log.trace("Converto l'associazione 'mittente'...");

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(mittenteShallowDto);

        if (convertedAccount != null) {
            notifica.setMittente(convertedAccount);
            convertedAccount.addNotificaInviata(notifica);
        }

        log.trace("'mittente' convertita correttamente.");
    }

    private void convertDestinatariShallow(Set<AccountShallowDto> destinatariShallowDto, Notifica notifica) throws InvalidParameterException {

        log.trace("Converto l'associazione 'destinatari'...");

        if (destinatariShallowDto != null) {
            for (AccountShallowDto accountShallowDto : destinatariShallowDto) {

                Account convertedAccount = relationsConverter.convertAccountShallowRelation(accountShallowDto);

                if (convertedAccount != null) {
                    notifica.addDestinatario(convertedAccount);
                    convertedAccount.addNotificaRicevuta(notifica);
                }
            }
        }

        log.trace("'destinatari' convertita correttamente.");
    }

    private void convertAstaAssociataShallow(AstaShallowDto astaAssociataShallowDto, Notifica notifica) throws InvalidParameterException {

        log.trace("Converto l'associazione 'astaAssociata'...");

        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaAssociataShallowDto);

        if (convertedAsta != null) {
            notifica.setAstaAssociata(convertedAsta);
            convertedAsta.addNotificaAssociata(notifica);
        }

        log.trace("'astaAssociata' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(NotificaDto updatedNotificaDto, Notifica existingNotifica) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di notifica richieste...");

        ifPresentUpdateDataInvio(updatedNotificaDto.getDataInvio(), existingNotifica);
        ifPresentUpdateOraInvio(updatedNotificaDto.getOraInvio(), existingNotifica);
        ifPresentUpdateMessaggio(updatedNotificaDto.getMessaggio(), existingNotifica);

        log.debug("Modifiche di notifica effettuate correttamente.");

        // Non è possibile modificare le associazioni "mittente", "destinatari", "astaAssociata" tramite la risorsa "notifiche"
    }

    private void ifPresentUpdateDataInvio(LocalDate updatedDataInvio, Notifica existingNotifica) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'dataInvio'...");

        if (updatedDataInvio != null) {
            this.checkDataInvioValid(updatedDataInvio);
            existingNotifica.setDataInvio(updatedDataInvio);
        }

        log.trace("'dataInvio' modificato correttamente.");
    }

    private void ifPresentUpdateOraInvio(LocalTime updatedOraInvio, Notifica existingNotifica) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'oraInvio'...");

        if (updatedOraInvio != null) {
            this.checkOraInvioValid(existingNotifica.getDataInvio(), updatedOraInvio);
            existingNotifica.setOraInvio(updatedOraInvio);
        }

        log.trace("'oraInvio' modificato correttamente.");
    }

    private void ifPresentUpdateMessaggio(String updatedMessaggio, Notifica existingNotifica) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'messaggio'...");

        if (updatedMessaggio != null) {
            this.checkMessaggioValid(updatedMessaggio);
            existingNotifica.setMessaggio(updatedMessaggio);
        }

        log.trace("'messaggio' modificato correttamente.");
    }
}
