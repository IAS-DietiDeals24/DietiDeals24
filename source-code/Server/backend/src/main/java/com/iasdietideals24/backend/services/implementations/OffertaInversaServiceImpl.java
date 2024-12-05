package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.OffertaInversaMapper;
import com.iasdietideals24.backend.repositories.OffertaInversaRepository;
import com.iasdietideals24.backend.services.OffertaDiVenditoreService;
import com.iasdietideals24.backend.services.OffertaInversaService;
import com.iasdietideals24.backend.services.helper.BuildNotice;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OffertaInversaServiceImpl implements OffertaInversaService {

    public static final String LOG_RECUPERO_OFFERTA = "Recupero l'offerta inversa dal database...";
    public static final String LOG_FOUND_OFFERTA = "foundOffertaInversa: {}";
    public static final String LOG_OFFERTA_RECUPERATA = "Offerta inversa recuperata dal database.";

    private final OffertaDiVenditoreService offertaDiVenditoreService;
    private final OffertaInversaMapper offertaInversaMapper;
    private final OffertaInversaRepository offertaInversaRepository;
    private final RelationsConverter relationsConverter;
    private final BuildNotice buildNotice;

    public OffertaInversaServiceImpl(OffertaDiVenditoreService offertaDiVenditoreService,
                                     OffertaInversaMapper offertaInversaMapper,
                                     OffertaInversaRepository offertaInversaRepository,
                                     RelationsConverter relationsConverter,
                                     BuildNotice buildNotice) {
        this.offertaDiVenditoreService = offertaDiVenditoreService;
        this.offertaInversaMapper = offertaInversaMapper;
        this.offertaInversaRepository = offertaInversaRepository;
        this.relationsConverter = relationsConverter;
        this.buildNotice = buildNotice;
    }

    @Override
    public OffertaInversaDto create(OffertaInversaDto nuovaOffertaInversaDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaOffertaInversaDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        OffertaInversa nuovaOffertaInversa = offertaInversaMapper.toEntity(nuovaOffertaInversaDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaOffertaInversa: {}", nuovaOffertaInversa);

        // Recuperiamo le associazioni
        convertRelations(nuovaOffertaInversaDto, nuovaOffertaInversa);

        log.trace("nuovaOffertaInversa: {}", nuovaOffertaInversa);

        log.debug("Salvo l'offerta inversa nel database...");

        // Registriamo l'entità
        OffertaInversa savedOffertaInversa = offertaInversaRepository.save(nuovaOffertaInversa);

        log.trace("savedOffertaInversa: {}", savedOffertaInversa);
        log.debug("Offerta inversa salvata correttamente nel database con id offerta {}...", savedOffertaInversa.getIdOfferta());

        // Invio le notifiche necessarie
        log.debug("Invio la notifica al proprietario dell'asta...");

        buildNotice.notifyNuovaOfferta(savedOffertaInversa);

        log.debug("Notifica inviata con successo.");

        return offertaInversaMapper.toDto(savedOffertaInversa);
    }

    @Override
    public Page<OffertaInversaDto> findAll(Pageable pageable) {

        log.debug("Recupero le offerte inverse dal database...");

        // Recuperiamo tutte le entità
        Page<OffertaInversa> foundOfferteInverse = offertaInversaRepository.findAll(pageable);

        log.trace("foundOfferteInverse: {}", foundOfferteInverse);
        log.debug("Offerte inverse recuperate dal database.");

        return foundOfferteInverse.map(offertaInversaMapper::toDto);
    }

    @Override
    public Page<OffertaInversaDto> findByAstaRiferimentoIdAsta(Long idAsta, Pageable pageable) {

        log.debug("Recupero le offerte inverse associate all'asta dal database...");
        log.trace("Id asta riferimento delle offerte da recuperare: {}", idAsta);

        // Recuperiamo tutte le entità
        Page<OffertaInversa> foundOfferteInverse = offertaInversaRepository.findByAstaRiferimento_IdAsta(idAsta, pageable);

        log.trace("foundOfferteInverse: {}", foundOfferteInverse);
        log.debug("Offerte inverse recuperate dal database.");

        return foundOfferteInverse.map(offertaInversaMapper::toDto);
    }

    @Override
    public Optional<OffertaInversaDto> findOne(Long idOfferta) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id offerta da recuperare: {}", idOfferta);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findById(idOfferta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaInversa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaInversa.map(offertaInversaMapper::toDto);
    }

    @Override
    public Optional<OffertaInversaDto> findMinByValoreAndAstaRiferimentoIdAstaIs(Long idAsta) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id asta dell'offerta minima da recuperare: {}", idAsta);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findMinByValoreAndAstaRiferimento_IdAstaIs(idAsta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaInversa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaInversa.map(offertaInversaMapper::toDto);
    }

    @Override
    public Optional<OffertaInversaDto> findMinByValoreAndAstaRiferimentoIdAstaIsAndVenditoreCollegatoIdAccountIs(Long idAsta, Long idAccount) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id asta dell'offerta minima da recuperare: {}", idAsta);
        log.trace("Id venditore dell'offerta minima da recuperare: {}", idAccount);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findMinByValoreAndAstaRiferimento_IdAstaIsAndVenditoreCollegato_IdAccountIs(idAsta, idAccount);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaInversa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaInversa.map(offertaInversaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idOfferta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return offertaInversaRepository.existsById(idOfferta);
    }

    @Override
    public OffertaInversaDto fullUpdate(Long idOfferta, OffertaInversaDto updatedOffertaInversaDto) throws InvalidParameterException {

        log.trace("Id offerta da sostituire: {}", idOfferta);

        updatedOffertaInversaDto.setIdOfferta(idOfferta);

        if (!offertaInversaRepository.existsById(idOfferta)) {
            log.warn("L'id offerta '{}' non corrisponde a nessuna offerta inversa esistente!", idOfferta);

            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta inversa esistente!");
        } else {
            return this.create(updatedOffertaInversaDto);
        }
    }

    @Override
    public OffertaInversaDto partialUpdate(Long idOfferta, OffertaInversaDto updatedOffertaInversaDto) throws InvalidParameterException {

        log.trace("Id offerta da aggiornare: {}", idOfferta);

        updatedOffertaInversaDto.setIdOfferta(idOfferta);

        log.debug(LOG_RECUPERO_OFFERTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findById(idOfferta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaInversa);
        log.debug(LOG_OFFERTA_RECUPERATA);

        if (foundOffertaInversa.isEmpty())
            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta inversa esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            OffertaInversa existingOffertaInversa = foundOffertaInversa.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedOffertaInversaDto, existingOffertaInversa);

            return offertaInversaMapper.toDto(offertaInversaRepository.save(existingOffertaInversa));
        }
    }

    @Override
    public void delete(Long idOfferta) {

        log.trace("Id offerta da eliminare: {}", idOfferta);
        log.debug("Elimino l'offerta inversa dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        offertaInversaRepository.deleteById(idOfferta);

        log.debug("Offerta inversa eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(OffertaInversaDto offertaInversaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di offerta inversa...");

        offertaDiVenditoreService.checkFieldsValid(offertaInversaDto);
        checkAstaRiferimentoValid(offertaInversaDto.getAstaRiferimentoShallow());

        log.debug("Integrità dei dati di offerta inversa verificata.");
    }

    private void checkAstaRiferimentoValid(AstaShallowDto astaRiferimentoShallow) throws InvalidParameterException {

        log.trace("Controllo che 'astaRiferimento' sia valido...");

        if (astaRiferimentoShallow == null)
            throw new InvalidParameterException("L'asta riferimento non può essere null!");

        log.trace("'astaRiferimento' valido.");
    }

    @Override
    public void convertRelations(OffertaInversaDto offertaInversaDto, OffertaInversa offertaInversa) throws InvalidParameterException {

        log.debug("Recupero le associazioni di offerta inversa...");

        offertaDiVenditoreService.convertRelations(offertaInversaDto, offertaInversa);
        convertAstaRiferimentoShallow(offertaInversaDto.getAstaRiferimentoShallow(), offertaInversa);

        log.debug("Associazioni di offerta inversa recuperate.");
    }

    private void convertAstaRiferimentoShallow(AstaShallowDto astaRiferimentoShallow, OffertaInversa offertaInversa) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'astaRiferimento'...");

        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaRiferimentoShallow);

        if (convertedAsta != null) {
            if (convertedAsta instanceof AstaInversa convertedAstaInversa) {
                offertaInversa.setAstaRiferimento(convertedAstaInversa);
                convertedAstaInversa.addOffertaRicevuta(offertaInversa);
            } else {
                throw new InvalidTypeException("Un'offerta inversa può riferirsi solo ad aste inverse!");
            }
        }

        log.trace("'astaRiferimento' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(OffertaInversaDto updatedOffertaInversaDto, OffertaInversa existingOffertaInversa) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di offerta inversa richieste...");

        offertaDiVenditoreService.updatePresentFields(updatedOffertaInversaDto, existingOffertaInversa);

        log.debug("Modifiche di offerta inversa effettuate correttamente.");

        // Non è possibile modificare l'associazione "astaRiferimento" tramite la risorsa "offerte/di-venditori/inverse"
    }
}