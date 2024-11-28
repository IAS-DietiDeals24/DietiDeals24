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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AstaSilenziosaServiceImpl implements AstaSilenziosaService {

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

        // Convertiamo a entità
        AstaSilenziosa nuovaAstaSilenziosa = astaSilenziosaMapper.toEntity(nuovaAstaSilenziosaDto);

        // Registriamo l'entità
        AstaSilenziosa savedAstaSilenziosa = astaSilenziosaRepository.save(nuovaAstaSilenziosa);

        return astaSilenziosaMapper.toDto(savedAstaSilenziosa);
    }

    @Override
    public Page<AstaSilenziosaDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<AstaSilenziosa> foundAsteSilenziose = astaSilenziosaRepository.findAll(pageable);

        return foundAsteSilenziose.map(astaSilenziosaMapper::toDto);
    }

    @Override
    public Optional<AstaSilenziosaDto> findOne(Long idAsta) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaSilenziosa> foundAstaSilenziosa = astaSilenziosaRepository.findById(idAsta);

        return foundAstaSilenziosa.map(astaSilenziosaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAsta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return astaSilenziosaRepository.existsById(idAsta);
    }

    @Override
    public AstaSilenziosaDto fullUpdate(Long idAsta, AstaSilenziosaDto updatedAstaSilenziosaDto) throws InvalidParameterException {

        updatedAstaSilenziosaDto.setIdAsta(idAsta);

        if (!astaSilenziosaRepository.existsById(idAsta))
            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta silenziosa esistente!");
        else {
            return this.create(updatedAstaSilenziosaDto);
        }
    }

    @Override
    public AstaSilenziosaDto partialUpdate(Long idAsta, AstaSilenziosaDto updatedAstaSilenziosaDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedAstaSilenziosaDto.setIdAsta(idAsta);

        Optional<AstaSilenziosa> foundAstaSilenziosa = astaSilenziosaRepository.findById(idAsta);
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

        // Eliminiamo l'entità con l'id passato per parametro
        astaSilenziosaRepository.deleteById(idAsta);
    }

    @Override
    public void checkFieldsValid(AstaSilenziosaDto astaSilenziosaDto) throws InvalidParameterException {
        astaDiVenditoreService.checkFieldsValid(astaSilenziosaDto);
    }

    @Override
    public void convertRelations(AstaSilenziosaDto astaSilenziosaDto, AstaSilenziosa astaSilenziosa) throws InvalidParameterException {
        astaDiVenditoreService.convertRelations(astaSilenziosaDto, astaSilenziosa);
        convertOfferteRicevute(astaSilenziosaDto.getOfferteRicevuteShallow(), astaSilenziosa);
    }

    private void convertOfferteRicevute(Set<OffertaShallowDto> offerteRicevuteShallow, AstaSilenziosa astaSilenziosa) throws IdNotFoundException, InvalidTypeException {
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
    }

    @Override
    public void updatePresentFields(AstaSilenziosaDto updatedAstaSilenziosaDto, AstaSilenziosa existingAstaSilenziosa) throws InvalidParameterException {
        astaDiVenditoreService.updatePresentFields(updatedAstaSilenziosaDto, existingAstaSilenziosa);

        // Non è possibile modificare l'associazione "offerteRicevute" tramite la risorsa "aste/di-venditori/silenziose"
    }
}
