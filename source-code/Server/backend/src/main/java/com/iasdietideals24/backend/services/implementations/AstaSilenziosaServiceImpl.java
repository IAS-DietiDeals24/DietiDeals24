package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.AstaSilenziosaMapper;
import com.iasdietideals24.backend.repositories.AstaSilenziosaRepository;
import com.iasdietideals24.backend.services.AstaDiVenditoreService;
import com.iasdietideals24.backend.services.AstaSilenziosaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class AstaSilenziosaServiceImpl implements AstaSilenziosaService {

    public static final String LOG_RECUPERO_ASTA_SILENZIOSA = "Recupero l'asta silenziosa dal database...";
    public static final String LOG_FOUND_ASTA_SILENZIOSA = "foundAstaSilenziosa: {}";
    public static final String LOG_ASTA_SILENZIOSA_RECUPERATA = "Asta silenziosa recuperata dal database.";

    private final AstaDiVenditoreService astaDiVenditoreService;
    private final AstaSilenziosaMapper astaSilenziosaMapper;
    private final AstaSilenziosaRepository astaSilenziosaRepository;
    private final RelationsConverter relationsConverter;

    public AstaSilenziosaServiceImpl(AstaDiVenditoreService astaDiVenditoreService,
                                     AstaSilenziosaMapper astaSilenziosaMapper,
                                     AstaSilenziosaRepository astaSilenziosaRepository,
                                     RelationsConverter relationsConverter) {
        this.astaDiVenditoreService = astaDiVenditoreService;
        this.astaSilenziosaMapper = astaSilenziosaMapper;
        this.astaSilenziosaRepository = astaSilenziosaRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public AstaSilenziosaDto create(AstaSilenziosaDto nuovaAstaSilenziosaDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaAstaSilenziosaDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        AstaSilenziosa nuovaAstaSilenziosa = astaSilenziosaMapper.toEntity(nuovaAstaSilenziosaDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaAstaSilenziosa: {}", nuovaAstaSilenziosa);

        // Recuperiamo le associazioni
        convertRelations(nuovaAstaSilenziosaDto, nuovaAstaSilenziosa);

        log.trace("nuovaAstaSilenziosa: {}", nuovaAstaSilenziosa);

        log.debug("Salvo l'asta silenziosa nel database...");

        // Registriamo l'entità
        AstaSilenziosa savedAstaSilenziosa = astaSilenziosaRepository.save(nuovaAstaSilenziosa);

        log.trace("savedAstaSilenziosa: {}", savedAstaSilenziosa);
        log.debug("Asta silenziosa salvata correttamente nel database con id asta {}...", savedAstaSilenziosa.getIdAsta());

        return astaSilenziosaMapper.toDto(savedAstaSilenziosa);
    }

    @Override
    public Page<AstaSilenziosaDto> findAll(Pageable pageable) {

        log.debug("Recupero le aste silenziose dal database...");

        // Recuperiamo tutte le entità
        Page<AstaSilenziosa> foundAsteSilenziose = astaSilenziosaRepository.findAll(pageable);

        log.trace("foundAsteSilenziose: {}", foundAsteSilenziose);
        log.debug("Aste silenziose recuperate dal database.");

        return foundAsteSilenziose.map(astaSilenziosaMapper::toDto);
    }

    @Override
    public Optional<AstaSilenziosaDto> findOne(Long idAsta) {

        log.trace("Id asta da recuperare: {}", idAsta);
        log.debug(LOG_RECUPERO_ASTA_SILENZIOSA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaSilenziosa> foundAstaSilenziosa = astaSilenziosaRepository.findById(idAsta);

        log.trace(LOG_FOUND_ASTA_SILENZIOSA, foundAstaSilenziosa);
        log.debug(LOG_ASTA_SILENZIOSA_RECUPERATA);

        return foundAstaSilenziosa.map(astaSilenziosaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAsta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return astaSilenziosaRepository.existsById(idAsta);
    }

    @Override
    public AstaSilenziosaDto fullUpdate(Long idAsta, AstaSilenziosaDto updatedAstaSilenziosaDto) throws InvalidParameterException {

        log.trace("Id asta da sostituire: {}", idAsta);

        updatedAstaSilenziosaDto.setIdAsta(idAsta);

        if (!astaSilenziosaRepository.existsById(idAsta))
            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta silenziosa esistente!");
        else {
            return this.create(updatedAstaSilenziosaDto);
        }
    }

    @Override
    public AstaSilenziosaDto partialUpdate(Long idAsta, AstaSilenziosaDto updatedAstaSilenziosaDto) throws InvalidParameterException {

        log.trace("Id asta da aggiornare: {}", idAsta);

        updatedAstaSilenziosaDto.setIdAsta(idAsta);

        log.debug(LOG_RECUPERO_ASTA_SILENZIOSA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaSilenziosa> foundAstaSilenziosa = astaSilenziosaRepository.findById(idAsta);

        log.trace(LOG_FOUND_ASTA_SILENZIOSA, foundAstaSilenziosa);
        log.debug(LOG_ASTA_SILENZIOSA_RECUPERATA);

        if (foundAstaSilenziosa.isEmpty())
            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta silenziosa esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            AstaSilenziosa existingAstaSilenziosa = foundAstaSilenziosa.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedAstaSilenziosaDto, existingAstaSilenziosa);

            return astaSilenziosaMapper.toDto(astaSilenziosaRepository.save(existingAstaSilenziosa));
        }
    }

    @Override
    public void delete(Long idAsta) {

        log.trace("Id asta da eliminare: {}", idAsta);
        log.debug("Elimino l'asta silenziosa dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        astaSilenziosaRepository.deleteById(idAsta);

        log.debug("Asta silenziosa eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(AstaSilenziosaDto astaSilenziosaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di asta silenziosa...");

        astaDiVenditoreService.checkFieldsValid(astaSilenziosaDto);

        log.debug("Integrità dei dati di asta silenziosa verificata.");
    }

    @Override
    public void convertRelations(AstaSilenziosaDto astaSilenziosaDto, AstaSilenziosa astaSilenziosa) throws InvalidParameterException {

        log.debug("Recupero le associazioni di asta silenziosa...");

        astaDiVenditoreService.convertRelations(astaSilenziosaDto, astaSilenziosa);
        convertOfferteRicevute(astaSilenziosaDto.getOfferteRicevuteShallow(), astaSilenziosa);

        log.debug("Associazioni di asta silenziosa recuperate.");
    }

    private void convertOfferteRicevute(Set<OffertaShallowDto> offerteRicevuteShallow, AstaSilenziosa astaSilenziosa) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'offerteRicevute'...");

        if (offerteRicevuteShallow != null) {
            for (OffertaShallowDto offertaShallowDto : offerteRicevuteShallow) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta instanceof OffertaSilenziosa convertedOffertaSilenziosa) {
                    astaSilenziosa.addOffertaRicevuta(convertedOffertaSilenziosa);
                    convertedOffertaSilenziosa.setAstaRiferimento(astaSilenziosa);
                } else {
                    throw new InvalidTypeException("Un'asta silenziosa può ricevere solo offerte silenziose!");
                }
            }
        }

        log.trace("'offerteRicevute' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AstaSilenziosaDto updatedAstaSilenziosaDto, AstaSilenziosa existingAstaSilenziosa) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di asta silenziosa richieste...");

        astaDiVenditoreService.updatePresentFields(updatedAstaSilenziosaDto, existingAstaSilenziosa);

        log.debug("Modifiche di asta silenziosa effettuate correttamente.");

        // Non è possibile modificare l'associazione "offerteRicevute" tramite la risorsa "aste/di-venditori/silenziose"
    }
}
