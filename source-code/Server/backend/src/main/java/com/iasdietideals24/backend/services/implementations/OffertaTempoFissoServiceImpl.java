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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OffertaTempoFissoServiceImpl implements OffertaTempoFissoService {

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

        // Convertiamo a entità
        OffertaTempoFisso nuovaOffertaTempoFisso = offertaTempoFissoMapper.toEntity(nuovaOffertaTempoFissoDto);

        // Registriamo l'entità
        OffertaTempoFisso savedOffertaTempoFisso = offertaTempoFissoRepository.save(nuovaOffertaTempoFisso);

        return offertaTempoFissoMapper.toDto(savedOffertaTempoFisso);
    }

    @Override
    public Page<OffertaTempoFissoDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<OffertaTempoFisso> foundOfferteTempoFisso = offertaTempoFissoRepository.findAll(pageable);

        return foundOfferteTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public Optional<OffertaTempoFissoDto> findOne(Long idOfferta) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findById(idOfferta);

        return foundOffertaTempoFisso.map(offertaTempoFissoMapper::toDto);
    }

    @Override
    public boolean isExists(Long idOfferta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return offertaTempoFissoRepository.existsById(idOfferta);
    }

    @Override
    public OffertaTempoFissoDto fullUpdate(Long idOfferta, OffertaTempoFissoDto updatedOffertaTempoFissoDto) throws InvalidParameterException {

        updatedOffertaTempoFissoDto.setIdOfferta(idOfferta);

        if (!offertaTempoFissoRepository.existsById(idOfferta))
            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta a tempo fisso esistente!");
        else {
            return this.create(updatedOffertaTempoFissoDto);
        }
    }

    @Override
    public OffertaTempoFissoDto partialUpdate(Long idOfferta, OffertaTempoFissoDto updatedOffertaTempoFissoDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedOffertaTempoFissoDto.setIdOfferta(idOfferta);

        Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findById(idOfferta);
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

        // Eliminiamo l'entità con l'id passato per parametro
        offertaTempoFissoRepository.deleteById(idOfferta);
    }

    @Override
    public void checkFieldsValid(OffertaTempoFissoDto offertaTempoFissoDto) throws InvalidParameterException {
        offertaDiCompratoreService.checkFieldsValid(offertaTempoFissoDto);
        checkAstaRiferimentoValid(offertaTempoFissoDto.getAstaRiferimentoShallow());
    }

    private void checkAstaRiferimentoValid(AstaShallowDto astaRiferimentoShallow) throws InvalidParameterException {
        if (astaRiferimentoShallow == null)
            throw new InvalidParameterException("L'asta riferimento non può essere null!");
    }

    @Override
    public void convertRelations(OffertaTempoFissoDto offertaTempoFissoDto, OffertaTempoFisso offertaTempoFisso) throws InvalidParameterException {
        offertaDiCompratoreService.convertRelations(offertaTempoFissoDto, offertaTempoFisso);
        convertAstaRiferimentoShallow(offertaTempoFissoDto.getAstaRiferimentoShallow(), offertaTempoFisso);
    }

    private void convertAstaRiferimentoShallow(AstaShallowDto astaRiferimentoShallow, OffertaTempoFisso offertaTempoFisso) throws IdNotFoundException, InvalidTypeException {
        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaRiferimentoShallow);

        if (convertedAsta != null) {
            if (convertedAsta instanceof AstaTempoFisso convertedAstaTempoFisso) {
                offertaTempoFisso.setAstaRiferimento(convertedAstaTempoFisso);
                convertedAstaTempoFisso.addOffertaRicevuta(offertaTempoFisso);
            } else {
                throw new InvalidTypeException("Un'offerta a tempo fisso può riferirsi solo ad aste a tempo fisso!");
            }
        }
    }

    @Override
    public void updatePresentFields(OffertaTempoFissoDto updatedOffertaTempoFissoDto, OffertaTempoFisso existingOffertaTempoFisso) throws InvalidParameterException {
        offertaDiCompratoreService.updatePresentFields(updatedOffertaTempoFissoDto, existingOffertaTempoFisso);

        // Non è possibile modificare l'associazione "astaRiferimento" tramite la risorsa "offerte/di-compratori/tempo-fisso"
    }
}
