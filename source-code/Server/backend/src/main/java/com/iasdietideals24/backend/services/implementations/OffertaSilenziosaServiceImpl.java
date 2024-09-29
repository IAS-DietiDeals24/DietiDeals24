package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.StatoOffertaSilenziosaDto;
import com.iasdietideals24.backend.mapstruct.mappers.OffertaSilenziosaMapper;
import com.iasdietideals24.backend.mapstruct.mappers.StatoOffertaSilenziosaMapper;
import com.iasdietideals24.backend.repositories.OffertaSilenziosaRepository;
import com.iasdietideals24.backend.services.OffertaDiCompratoreService;
import com.iasdietideals24.backend.services.OffertaSilenziosaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OffertaSilenziosaServiceImpl implements OffertaSilenziosaService {

    private final OffertaDiCompratoreService offertaDiCompratoreService;
    private final StatoOffertaSilenziosaMapper statoOffertaSilenziosaMapper;
    private final OffertaSilenziosaMapper offertaSilenziosaMapper;
    private final OffertaSilenziosaRepository offertaSilenziosaRepository;
    private final RelationsConverter relationsConverter;

    public OffertaSilenziosaServiceImpl(OffertaDiCompratoreService offertaDiCompratoreService,
                                        StatoOffertaSilenziosaMapper statoOffertaSilenziosaMapper,
                                        OffertaSilenziosaMapper offertaSilenziosaMapper,
                                        OffertaSilenziosaRepository offertaSilenziosaRepository,
                                        RelationsConverter relationsConverter) {
        this.offertaDiCompratoreService = offertaDiCompratoreService;
        this.statoOffertaSilenziosaMapper = statoOffertaSilenziosaMapper;
        this.offertaSilenziosaMapper = offertaSilenziosaMapper;
        this.offertaSilenziosaRepository = offertaSilenziosaRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public OffertaSilenziosaDto create(OffertaSilenziosaDto nuovaOffertaSilenziosaDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaOffertaSilenziosaDto);

        // Convertiamo a entità
        OffertaSilenziosa nuovaOffertaSilenziosa = offertaSilenziosaMapper.toEntity(nuovaOffertaSilenziosaDto);

        // Registriamo l'entità
        OffertaSilenziosa savedOffertaSilenziosa = offertaSilenziosaRepository.save(nuovaOffertaSilenziosa);

        return offertaSilenziosaMapper.toDto(savedOffertaSilenziosa);
    }

    @Override
    public Page<OffertaSilenziosaDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<OffertaSilenziosa> foundOfferteSilenziose = offertaSilenziosaRepository.findAll(pageable);

        return foundOfferteSilenziose.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public Optional<OffertaSilenziosaDto> findOne(Long idOfferta) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findById(idOfferta);

        return foundOffertaSilenziosa.map(offertaSilenziosaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idOfferta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return offertaSilenziosaRepository.existsById(idOfferta);
    }

    @Override
    public OffertaSilenziosaDto fullUpdate(Long idOfferta, OffertaSilenziosaDto updatedOffertaSilenziosaDto) throws InvalidParameterException {

        updatedOffertaSilenziosaDto.setIdOfferta(idOfferta);

        if (!offertaSilenziosaRepository.existsById(idOfferta))
            throw new UpdateRuntimeException("L'id offerta \"" + idOfferta + "\" non corrisponde a nessuna offerta silenziosa esistente!");
        else {
           return this.create(updatedOffertaSilenziosaDto);
        }
    }

    @Override
    public OffertaSilenziosaDto partialUpdate(Long idOfferta, OffertaSilenziosaDto updatedOffertaSilenziosaDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedOffertaSilenziosaDto.setIdOfferta(idOfferta);

        Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findById(idOfferta);
        if (foundOffertaSilenziosa.isEmpty())
            throw new UpdateRuntimeException("L'id offerta \"" + idOfferta + "\" non corrisponde a nessuna offerta silenziosa esistente!");
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

        // Eliminiamo l'entità con l'id passato per parametro
        offertaSilenziosaRepository.deleteById(idOfferta);
    }

    @Override
    public void checkFieldsValid(OffertaSilenziosaDto offertaSilenziosaDto) throws InvalidParameterException {
        offertaDiCompratoreService.checkFieldsValid(offertaSilenziosaDto);
        checkAstaRiferimentoValid(offertaSilenziosaDto.getAstaRiferimentoShallow());
    }

    private void checkAstaRiferimentoValid(AstaShallowDto astaRiferimentoShallow) throws InvalidParameterException {
        if (astaRiferimentoShallow == null)
            throw new InvalidParameterException("L'asta riferimento non può essere null!");
    }

    @Override
    public void convertRelations(OffertaSilenziosaDto offertaSilenziosaDto, OffertaSilenziosa offertaSilenziosa) throws InvalidParameterException {
        offertaDiCompratoreService.convertRelations(offertaSilenziosaDto, offertaSilenziosa);
        convertAstaRiferimentoShallow(offertaSilenziosaDto.getAstaRiferimentoShallow(), offertaSilenziosa);
    }

    private void convertAstaRiferimentoShallow(AstaShallowDto astaRiferimentoShallow, OffertaSilenziosa offertaSilenziosa) throws IdNotFoundException, InvalidTypeException {
        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaRiferimentoShallow);

        if (convertedAsta != null) {
            if (convertedAsta instanceof AstaSilenziosa convertedAstaSilenziosa) {
                offertaSilenziosa.setAstaRiferimento(convertedAstaSilenziosa);
                convertedAstaSilenziosa.addOffertaRicevuta(offertaSilenziosa);
            } else {
                throw new InvalidTypeException("Un'offerta silenziosa può riferirsi solo ad aste silenziose!");
            }
        }
    }

    @Override
    public void updatePresentFields(OffertaSilenziosaDto updatedOffertaSilenziosaDto, OffertaSilenziosa existingOffertaSilenziosa) throws InvalidParameterException {
        offertaDiCompratoreService.updatePresentFields(updatedOffertaSilenziosaDto, existingOffertaSilenziosa);
        ifPresentUpdateStato(updatedOffertaSilenziosaDto.getStato(), existingOffertaSilenziosa);

        // Non è possibile modificare l'associazione "astaRiferimento" tramite la risorsa "offerte/di-compratori/silenziose"
    }

    private void ifPresentUpdateStato(StatoOffertaSilenziosaDto stato, OffertaSilenziosa existingOffertaSilenziosa) {
        if (stato != null) {
            existingOffertaSilenziosa.setStato(statoOffertaSilenziosaMapper.toEntity(stato));
        }
    }
}
