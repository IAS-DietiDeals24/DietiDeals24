package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.OffertaTempoFissoMapper;
import com.iasdietideals24.backend.repositories.OffertaTempoFissoRepository;
import com.iasdietideals24.backend.services.OffertaDiCompratoreService;
import com.iasdietideals24.backend.services.OffertaTempoFissoService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OffertaTempoFissoServiceImpl implements OffertaTempoFissoService {

    public static final String LOG_RECUPERO_OFFERTA = "Recupero l'offerta a tempo fisso dal database...";
    public static final String LOG_FOUND_OFFERTA = "foundOffertaTempoFisso: {}";
    public static final String LOG_OFFERTA_RECUPERATA = "Offerta a tempo fisso recuperata dal database.";

    private final OffertaDiCompratoreService offertaDiCompratoreService;
    private final OffertaTempoFissoMapper offertaTempoFissoMapper;
    private final OffertaTempoFissoRepository offertaTempoFissoRepository;
    private final RelationsConverter relationsConverter;

    public OffertaTempoFissoServiceImpl(OffertaDiCompratoreService offertaDiCompratoreService,
                                     OffertaTempoFissoMapper offertaTempoFissoMapper,
                                     OffertaTempoFissoRepository offertaTempoFissoRepository,
                                     RelationsConverter relationsConverter) {
        this.offertaDiCompratoreService = offertaDiCompratoreService;
        this.offertaTempoFissoMapper = offertaTempoFissoMapper;
        this.offertaTempoFissoRepository = offertaTempoFissoRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public OffertaTempoFissoDto create(OffertaTempoFissoDto nuovaOffertaTempoFissoDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaOffertaTempoFissoDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        OffertaTempoFisso nuovaOffertaTempoFisso = offertaTempoFissoMapper.toEntity(nuovaOffertaTempoFissoDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaOffertaTempoFisso: {}", nuovaOffertaTempoFisso);

        // Recuperiamo le associazioni
        convertRelations(nuovaOffertaTempoFissoDto, nuovaOffertaTempoFisso);

        log.trace("nuovaOffertaTempoFisso: {}", nuovaOffertaTempoFisso);

        log.debug("Salvo l'offerta a tempo fisso nel database...");

        // Registriamo l'entità
        OffertaTempoFisso savedOffertaTempoFisso = offertaTempoFissoRepository.save(nuovaOffertaTempoFisso);

        log.trace("savedOffertaTempoFisso: {}", savedOffertaTempoFisso);
        log.debug("Offerta a tempo fisso salvata correttamente nel database con id offerta {}...", savedOffertaTempoFisso.getIdOfferta());

        return offertaTempoFissoMapper.toDto(savedOffertaTempoFisso);
    }

    @Override
    public Page<OffertaTempoFissoDto> findAll(Pageable pageable) {

        log.debug("Recupero le offerte a tempo fisso dal database...");

        // Recuperiamo tutte le entità
        Page<OffertaTempoFisso> foundOfferteTempoFisso = offertaTempoFissoRepository.findAll(pageable);

        log.trace("foundOfferteTempoFisso: {}", foundOfferteTempoFisso);
        log.debug("Offerte a tempo fisso recuperate dal database.");

        return foundOfferteTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public Page<OffertaTempoFissoDto> findByAstaRiferimentoIdAsta(Long idAsta, Pageable pageable) {

        log.debug("Recupero le offerte a tempo fisso associate all'asta dal database...");
        log.trace("Id asta riferimento delle offerte da recuperare: {}", idAsta);

        // Recuperiamo tutte le entità
        Page<OffertaTempoFisso> foundOfferteTempoFisso = offertaTempoFissoRepository.findByAstaRiferimento_IdAsta(idAsta, pageable);

        log.trace("foundOfferteTempoFisso: {}", foundOfferteTempoFisso);
        log.debug("Offerte a tempo fisso recuperate dal database.");

        return foundOfferteTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public Optional<OffertaTempoFissoDto> findOne(Long idOfferta) {

        log.trace("Id offerta da recuperare: {}", idOfferta);
        log.debug(LOG_RECUPERO_OFFERTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findById(idOfferta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaTempoFisso);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public Optional<OffertaTempoFissoDto> findMaxByValoreAndAstaRiferimentoIdAstaIs(Long idAsta) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id asta dell'offerta massima da recuperare: {}", idAsta);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findMaxByValoreAndAstaRiferimento_IdAstaIs(idAsta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaTempoFisso);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public Optional<OffertaTempoFissoDto> findMaxByValoreAndAstaRiferimentoIdAstaIsAndCompratoreCollegatoIdAccountIs(Long idAsta, Long idAccount) {

        log.debug(LOG_RECUPERO_OFFERTA);
        log.trace("Id asta dell'offerta massima da recuperare: {}", idAsta);
        log.trace("Id compratore dell'offerta massima da recuperare: {}", idAccount);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findMaxByValoreAndAstaRiferimento_IdAstaIsAndCompratoreCollegato_IdAccountIs(idAsta, idAccount);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaTempoFisso);
        log.debug(LOG_OFFERTA_RECUPERATA);

        return foundOffertaTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public boolean isExists(Long idOfferta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return offertaTempoFissoRepository.existsById(idOfferta);
    }

    @Override
    public OffertaTempoFissoDto fullUpdate(Long idOfferta, OffertaTempoFissoDto updatedOffertaTempoFissoDto) throws InvalidParameterException {

        log.trace("Id offerta da sostituire: {}", idOfferta);

        updatedOffertaTempoFissoDto.setIdOfferta(idOfferta);

        if (!offertaTempoFissoRepository.existsById(idOfferta)) {
            log.warn("L'id offerta '{}' non corrisponde a nessuna offerta a tempo fisso esistente!", idOfferta);

            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta a tempo fisso esistente!");
        } else {
            return this.create(updatedOffertaTempoFissoDto);
        }
    }

    @Override
    public OffertaTempoFissoDto partialUpdate(Long idOfferta, OffertaTempoFissoDto updatedOffertaTempoFissoDto) throws InvalidParameterException {

        log.trace("Id offerta da aggiornare: {}", idOfferta);

        updatedOffertaTempoFissoDto.setIdOfferta(idOfferta);

        log.debug(LOG_RECUPERO_OFFERTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findById(idOfferta);

        log.trace(LOG_FOUND_OFFERTA, foundOffertaTempoFisso);
        log.debug(LOG_OFFERTA_RECUPERATA);

        if (foundOffertaTempoFisso.isEmpty())
            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta a tempo fisso esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            OffertaTempoFisso existingOffertaTempoFisso = foundOffertaTempoFisso.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedOffertaTempoFissoDto, existingOffertaTempoFisso);

            return offertaTempoFissoMapper.toDto(offertaTempoFissoRepository.save(existingOffertaTempoFisso));
        }
    }

    @Override
    public void delete(Long idOfferta) {

        log.trace("Id offerta da eliminare: {}", idOfferta);
        log.debug("Elimino l'offerta a tempo fisso dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        offertaTempoFissoRepository.deleteById(idOfferta);

        log.debug("Offerta a tempo fisso eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(OffertaTempoFissoDto offertaTempoFissoDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di offerta a tempo fisso...");

        offertaDiCompratoreService.checkFieldsValid(offertaTempoFissoDto);
        checkAstaRiferimentoValid(offertaTempoFissoDto.getAstaRiferimentoShallow());

        log.debug("Integrità dei dati di offerta a tempo fisso verificata.");
    }

    private void checkAstaRiferimentoValid(AstaShallowDto astaRiferimentoShallow) throws InvalidParameterException {

        log.trace("Controllo che 'astaRiferimento' sia valido...");

        if (astaRiferimentoShallow == null)
            throw new InvalidParameterException("L'asta riferimento non può essere null!");

        log.trace("'astaRiferimento' valido.");
    }

    @Override
    public void convertRelations(OffertaTempoFissoDto offertaTempoFissoDto, OffertaTempoFisso offertaTempoFisso) throws InvalidParameterException {

        log.debug("Recupero le associazioni di offerta a tempo fisso...");

        offertaDiCompratoreService.convertRelations(offertaTempoFissoDto, offertaTempoFisso);
        convertAstaRiferimentoShallow(offertaTempoFissoDto.getAstaRiferimentoShallow(), offertaTempoFisso);

        log.debug("Associazioni di offerta a tempo fisso recuperate.");
    }

    private void convertAstaRiferimentoShallow(AstaShallowDto astaRiferimentoShallow, OffertaTempoFisso offertaTempoFisso) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'astaRiferimento'...");

        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaRiferimentoShallow);

        if (convertedAsta != null) {
            if (convertedAsta instanceof AstaTempoFisso convertedAstaTempoFisso) {
                offertaTempoFisso.setAstaRiferimento(convertedAstaTempoFisso);
                convertedAstaTempoFisso.addOffertaRicevuta(offertaTempoFisso);
            } else {
                throw new InvalidTypeException("Un'offerta a tempo fisso può riferirsi solo ad aste a tempo fisso!");
            }
        }

        log.trace("'astaRiferimento' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(OffertaTempoFissoDto updatedOffertaTempoFissoDto, OffertaTempoFisso existingOffertaTempoFisso) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di offerta a tempo fisso richieste...");

        offertaDiCompratoreService.updatePresentFields(updatedOffertaTempoFissoDto, existingOffertaTempoFisso);

        log.debug("Modifiche di offerta a tempo fisso effettuate correttamente.");

        // Non è possibile modificare l'associazione "astaRiferimento" tramite la risorsa "offerte/di-compratori/tempo-fisso"
    }
}