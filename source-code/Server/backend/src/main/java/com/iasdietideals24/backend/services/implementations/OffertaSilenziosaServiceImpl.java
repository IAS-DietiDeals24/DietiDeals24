package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import com.iasdietideals24.backend.entities.utilities.StatoOffertaSilenziosa;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.OffertaSilenziosaMapper;
import com.iasdietideals24.backend.mapstruct.mappers.StatoOffertaSilenziosaMapper;
import com.iasdietideals24.backend.repositories.OffertaSilenziosaRepository;
import com.iasdietideals24.backend.scheduled.AstaScheduler;
import com.iasdietideals24.backend.services.OffertaDiCompratoreService;
import com.iasdietideals24.backend.services.OffertaSilenziosaService;
import com.iasdietideals24.backend.services.helper.BuildNotice;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class OffertaSilenziosaServiceImpl implements OffertaSilenziosaService {

    public static final String LOG_RECUPERO_OFFERTA = "Recupero l'offerta silenziosa dal database...";
    public static final String LOG_FOUND_OFFERTA = "foundOffertaSilenziosa: {}";
    public static final String LOG_OFFERTA_RECUPERATA = "Offerta silenziosa recuperata dal database.";

    private final OffertaDiCompratoreService offertaDiCompratoreService;

    private final StatoOffertaSilenziosaMapper statoOffertaSilenziosaMapper;
    private final OffertaSilenziosaMapper offertaSilenziosaMapper;
    private final OffertaSilenziosaRepository offertaSilenziosaRepository;

    private final RelationsConverter relationsConverter;
    private final BuildNotice buildNotice;
    private final AstaScheduler astaScheduler;

    public OffertaSilenziosaServiceImpl(OffertaDiCompratoreService offertaDiCompratoreService,
                                        StatoOffertaSilenziosaMapper statoOffertaSilenziosaMapper,
                                        OffertaSilenziosaMapper offertaSilenziosaMapper,
                                        OffertaSilenziosaRepository offertaSilenziosaRepository,
                                        RelationsConverter relationsConverter,
                                        BuildNotice buildNotice,
                                        AstaScheduler astaScheduler) {
        this.offertaDiCompratoreService = offertaDiCompratoreService;
        this.statoOffertaSilenziosaMapper = statoOffertaSilenziosaMapper;
        this.offertaSilenziosaMapper = offertaSilenziosaMapper;
        this.offertaSilenziosaRepository = offertaSilenziosaRepository;
        this.relationsConverter = relationsConverter;
        this.buildNotice = buildNotice;
        this.astaScheduler = astaScheduler;
    }

    @Override
    public OffertaSilenziosaDto create(OffertaSilenziosaDto nuovaOffertaSilenziosaDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaOffertaSilenziosaDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        OffertaSilenziosa nuovaOffertaSilenziosa = offertaSilenziosaMapper.toEntity(nuovaOffertaSilenziosaDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaOffertaSilenziosa: {}", nuovaOffertaSilenziosa);

        // Recuperiamo le associazioni
        convertRelations(nuovaOffertaSilenziosaDto, nuovaOffertaSilenziosa);

        log.trace("nuovaOffertaSilenziosa: {}", nuovaOffertaSilenziosa);

        // Controlliamo che l'asta a cui vogliamo riferirci sia attiva
        checkAstaActive(nuovaOffertaSilenziosa);

        // Controlliamo che il proprietario dell'asta a cui vogliamo riferirci non sia un altro nostro account
        checkProprietarioAstaNotMe(nuovaOffertaSilenziosa);

        log.debug("Salvo l'offerta silenziosa nel database...");

        // Registriamo l'entità
        OffertaSilenziosa savedOffertaSilenziosa = offertaSilenziosaRepository.save(nuovaOffertaSilenziosa);

        log.trace("savedOffertaSilenziosa: {}", savedOffertaSilenziosa);
        log.debug("Offerta silenziosa salvata correttamente nel database con id offerta {}...", savedOffertaSilenziosa.getIdOfferta());

        // Invio le notifiche necessarie
        log.debug("Invio la notifica al proprietario dell'asta...");

        buildNotice.notifyNuovaOfferta(savedOffertaSilenziosa);

        log.debug("Notifica inviata con successo.");

        doTasksForStatoOfferta(savedOffertaSilenziosa);

        return offertaSilenziosaMapper.toDto(savedOffertaSilenziosa);
    }

    @Override
    public Page<OffertaSilenziosaDto> findAll(Pageable pageable) {

        log.debug("Recupero le offerte silenziose dal database...");

        // Recuperiamo tutte le entità
        Page<OffertaSilenziosa> foundOfferteSilenziose = offertaSilenziosaRepository.findAll(pageable);

        log.trace("foundOfferteSilenziose: {}", foundOfferteSilenziose);
        log.debug("Offerte silenziose recuperate dal database.");

        return foundOfferteSilenziose.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public Page<OffertaSilenziosaDto> findByAstaRiferimentoIdAsta(Long idAsta, Pageable pageable) {

        log.debug("Recupero le offerte silenziose associate all'asta dal database...");
        log.trace("Id asta riferimento delle offerte da recuperare: {}", idAsta);

        // Recuperiamo tutte le entità
        Page<OffertaSilenziosa> foundOfferteSilenziose = offertaSilenziosaRepository.findByAstaRiferimento_IdAsta(idAsta, pageable);

        log.trace("foundOfferteSilenziose: {}", foundOfferteSilenziose);
        log.debug("Offerte silenziose recuperate dal database.");

        return foundOfferteSilenziose.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public Optional<OffertaSilenziosaDto> findOne(Long idOfferta) {

        log.trace("Id offerta da recuperare: {}", idOfferta);
        log.debug(LOG_RECUPERO_OFFERTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findById(idOfferta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaSilenziosa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaSilenziosa.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public Optional<OffertaSilenziosaDto> findMaxByValoreAndAstaRiferimentoIdAstaIs(Long idAsta) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id asta dell'offerta massima da recuperare: {}", idAsta);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findMaxByValoreAndAstaRiferimento_IdAstaIs(idAsta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaSilenziosa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaSilenziosa.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public Optional<OffertaSilenziosaDto> findMaxByValoreAndAstaRiferimentoIdAstaIsAndCompratoreCollegatoIdAccountIs(Long idAsta, Long idAccount) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id asta dell'offerta massima da recuperare: {}", idAsta);
        log.trace("Id compratore dell'offerta massima da recuperare: {}", idAccount);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findMaxByValoreAndAstaRiferimento_IdAstaIsAndCompratoreCollegato_IdAccountIs(idAsta, idAccount);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaSilenziosa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaSilenziosa.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idOfferta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return offertaSilenziosaRepository.existsById(idOfferta);
    }

    @Override
    public OffertaSilenziosaDto fullUpdate(Long idOfferta, OffertaSilenziosaDto updatedOffertaSilenziosaDto) throws InvalidParameterException {

        log.trace("Id offerta da sostituire: {}", idOfferta);

        updatedOffertaSilenziosaDto.setIdOfferta(idOfferta);

        if (!offertaSilenziosaRepository.existsById(idOfferta)) {
            log.warn("L'id offerta '{}' non corrisponde a nessuna offerta silenziosa esistente!", idOfferta);

            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta silenziosa esistente!");
        } else {
            return this.create(updatedOffertaSilenziosaDto);
        }
    }

    @Override
    public OffertaSilenziosaDto partialUpdate(Long idOfferta, OffertaSilenziosaDto updatedOffertaSilenziosaDto) throws InvalidParameterException {

        log.trace("Id offerta da aggiornare: {}", idOfferta);

        updatedOffertaSilenziosaDto.setIdOfferta(idOfferta);

        log.debug(LOG_RECUPERO_OFFERTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findById(idOfferta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaSilenziosa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        if (foundOffertaSilenziosa.isEmpty())
            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta silenziosa esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            OffertaSilenziosa existingOffertaSilenziosa = foundOffertaSilenziosa.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedOffertaSilenziosaDto, existingOffertaSilenziosa);

            return offertaSilenziosaMapper.toDto(offertaSilenziosaRepository.save(existingOffertaSilenziosa));
        }
    }

    @Override
    public void delete(Long idOfferta) {

        log.trace("Id offerta da eliminare: {}", idOfferta);
        log.debug("Elimino l'offerta silenziosa dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        offertaSilenziosaRepository.deleteById(idOfferta);

        log.debug("Offerta silenziosa eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(OffertaSilenziosaDto offertaSilenziosaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di offerta silenziosa...");

        offertaDiCompratoreService.checkFieldsValid(offertaSilenziosaDto);
        checkStatoValid(offertaSilenziosaDto.getStato());
        checkAstaRiferimentoValid(offertaSilenziosaDto.getAstaRiferimentoShallow());

        log.debug("Integrità dei dati di offerta silenziosa verificata.");
    }

    private void checkStatoValid(String stato) throws InvalidParameterException {

        log.trace("Controllo che 'stato' sia valido...");

        if (stato == null)
            throw new InvalidParameterException("Lo stato non può essere null!");
        else if (stato.isBlank())
            throw new InvalidParameterException("Lo stato non può essere vuoto!");

        log.trace("'stato' valido.");
    }

    private void checkAstaRiferimentoValid(AstaShallowDto astaRiferimentoShallow) throws InvalidParameterException {

        log.trace("Controllo che 'astaRiferimento' sia valido...");

        if (astaRiferimentoShallow == null)
            throw new InvalidParameterException("L'asta riferimento non può essere null!");

        log.trace("'astaRiferimento' valido.");
    }

    @Override
    public void convertRelations(OffertaSilenziosaDto offertaSilenziosaDto, OffertaSilenziosa offertaSilenziosa) throws InvalidParameterException {

        log.debug("Recupero le associazioni di offerta silenziosa...");

        offertaDiCompratoreService.convertRelations(offertaSilenziosaDto, offertaSilenziosa);
        convertAstaRiferimentoShallow(offertaSilenziosaDto.getAstaRiferimentoShallow(), offertaSilenziosa);

        log.debug("Associazioni di offerta silenziosa recuperate.");
    }

    private void convertAstaRiferimentoShallow(AstaShallowDto astaRiferimentoShallow, OffertaSilenziosa offertaSilenziosa) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'astaRiferimento'...");

        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaRiferimentoShallow);

        if (convertedAsta != null) {
            if (convertedAsta instanceof AstaSilenziosa convertedAstaSilenziosa) {
                offertaSilenziosa.setAstaRiferimento(convertedAstaSilenziosa);
                convertedAstaSilenziosa.addOffertaRicevuta(offertaSilenziosa);
            } else {
                throw new InvalidTypeException("Un'offerta silenziosa può riferirsi solo ad aste silenziose!");
            }
        }

        log.trace("'astaRiferimento' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(OffertaSilenziosaDto updatedOffertaSilenziosaDto, OffertaSilenziosa existingOffertaSilenziosa) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di offerta silenziosa richieste...");

        offertaDiCompratoreService.updatePresentFields(updatedOffertaSilenziosaDto, existingOffertaSilenziosa);
        ifPresentUpdateStato(updatedOffertaSilenziosaDto.getStato(), existingOffertaSilenziosa);

        doTasksForStatoOfferta(existingOffertaSilenziosa);

        log.debug("Modifiche di offerta silenziosa effettuate correttamente.");

        // Non è possibile modificare l'associazione "astaRiferimento" tramite la risorsa "offerte/di-compratori/silenziose"
    }

    private void ifPresentUpdateStato(String stato, OffertaSilenziosa existingOffertaSilenziosa) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'stato'...");

        if (stato != null) {
            checkStatoValid(stato);
            existingOffertaSilenziosa.setStato(statoOffertaSilenziosaMapper.toEntity(stato));
        }

        log.trace("'stato' modificato correttamente.");
    }

    private void checkAstaActive(OffertaSilenziosa nuovaOffertaSilenziosa) throws InvalidParameterException {

        if (nuovaOffertaSilenziosa != null) {

            AstaSilenziosa astaSilenziosa = nuovaOffertaSilenziosa.getAstaRiferimento();

            astaScheduler.updateExpiredAste();

            if (astaSilenziosa != null && astaSilenziosa.getStato().equals(StatoAsta.CLOSED)) {
                log.warn("L'asta '{}' a cui si vuole fare l'offerta è già terminata!", astaSilenziosa.getIdAsta());

                throw new InvalidParameterException("L'asta '" + astaSilenziosa.getIdAsta() + "' a cui si vuole fare l'offerta è già terminata!");
            }
        }
    }

    private void checkProprietarioAstaNotMe(OffertaSilenziosa nuovaOffertaSilenziosa) throws InvalidParameterException {

        if (nuovaOffertaSilenziosa != null) {

            AstaSilenziosa astaSilenziosa = nuovaOffertaSilenziosa.getAstaRiferimento();

            if (astaSilenziosa != null) {

                Profilo profiloProprietario = astaSilenziosa.getProprietario().getProfilo();

                if (profiloProprietario != null && profiloProprietario.equals(nuovaOffertaSilenziosa.getCompratoreCollegato().getProfilo())) {
                    log.warn("Il proprietario dell'asta '{}' a cui si vuole fare l'offerta sei tu!", astaSilenziosa.getIdAsta());

                    throw new InvalidParameterException("Il proprietario dell'asta '" + astaSilenziosa.getIdAsta() + "' a cui si vuole fare l'offerta sei tu!");
                }
            }
        }
    }

    private void doTasksForStatoOfferta(OffertaSilenziosa offertaSilenziosa) {

        if (offertaSilenziosa != null) {

            log.debug("Controllo lo stato dell'offerta...");

            if (offertaSilenziosa.getStato().equals(StatoOffertaSilenziosa.ACCEPTED)) {
                acceptedOffertaTasks(offertaSilenziosa);
            } else if (offertaSilenziosa.getStato().equals(StatoOffertaSilenziosa.REJECTED)) {
                rejectedOffertaTasks(offertaSilenziosa);
            }
        }
    }

    private void acceptedOffertaTasks(OffertaSilenziosa offertaSilenziosa) {

        log.debug("L'offerta è stata accettata. Chiudo l'asta...");

        AstaSilenziosa astaSilenziosa = offertaSilenziosa.getAstaRiferimento();
        astaSilenziosa.setStato(StatoAsta.CLOSED);

        log.debug("Asta chiusa. Rifiuto tutte le offerte in attesa...");

        Set<OffertaSilenziosa> offerteRifiutate = new HashSet<>();
        for (OffertaSilenziosa pendingOfferta : astaSilenziosa.getOfferteRicevute()) {

            if (pendingOfferta.getStato().equals(StatoOffertaSilenziosa.PENDING)) {
                pendingOfferta.setStato(StatoOffertaSilenziosa.REJECTED);
                offerteRifiutate.add(pendingOfferta);
            }
        }

        log.debug("Offerte rifiutate. Invio le notifiche agli offerenti...");

        buildNotice.notifyOffertaSilenziosaRifiutata(offerteRifiutate);
        buildNotice.notifyOffertaSilenziosaAccettata(offertaSilenziosa);

        log.debug("Notifiche inviate con successo.");
    }

    private void rejectedOffertaTasks(OffertaSilenziosa offertaSilenziosa) {

        log.debug("L'offerta è stata rifiutata. Invio la notifica all'offerente...");

        buildNotice.notifyOffertaSilenziosaRifiutata(offertaSilenziosa);

        log.debug("Notifica inviata con successo.");
    }
}
