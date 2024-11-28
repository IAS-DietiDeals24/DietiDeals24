package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.OffertaInversa;
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
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OffertaInversaServiceImpl implements OffertaInversaService {

    private final OffertaDiVenditoreService offertaDiVenditoreService;
    private final OffertaInversaMapper offertaInversaMapper;
    private final OffertaInversaRepository offertaInversaRepository;
    private final RelationsConverter relationsConverter;

    public OffertaInversaServiceImpl(OffertaDiVenditoreService offertaDiVenditoreService,
                                     OffertaInversaMapper offertaInversaMapper,
                                     OffertaInversaRepository offertaInversaRepository,
                                     RelationsConverter relationsConverter) {
        this.offertaDiVenditoreService = offertaDiVenditoreService;
        this.offertaInversaMapper = offertaInversaMapper;
        this.offertaInversaRepository = offertaInversaRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public OffertaInversaDto create(OffertaInversaDto nuovaOffertaInversaDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaOffertaInversaDto);

        // Convertiamo a entità
        OffertaInversa nuovaOffertaInversa = offertaInversaMapper.toEntity(nuovaOffertaInversaDto);

        // Registriamo l'entità
        OffertaInversa savedOffertaInversa = offertaInversaRepository.save(nuovaOffertaInversa);

        return offertaInversaMapper.toDto(savedOffertaInversa);
    }

    @Override
    public Page<OffertaInversaDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<OffertaInversa> foundOfferteInverse = offertaInversaRepository.findAll(pageable);

        return foundOfferteInverse.map(offertaInversaMapper::toDto);
    }

    @Override
    public Optional<OffertaInversaDto> findOne(Long idOfferta) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findById(idOfferta);

        return foundOffertaInversa.map(offertaInversaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idOfferta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return offertaInversaRepository.existsById(idOfferta);
    }

    @Override
    public OffertaInversaDto fullUpdate(Long idOfferta, OffertaInversaDto updatedOffertaInversaDto) throws InvalidParameterException {

        updatedOffertaInversaDto.setIdOfferta(idOfferta);

        if (!offertaInversaRepository.existsById(idOfferta))
            throw new UpdateRuntimeException("L'id offerta '" + idOfferta + "' non corrisponde a nessuna offerta inversa esistente!");
        else {
            return this.create(updatedOffertaInversaDto);
        }
    }

    @Override
    public OffertaInversaDto partialUpdate(Long idOfferta, OffertaInversaDto updatedOffertaInversaDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedOffertaInversaDto.setIdOfferta(idOfferta);

        Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findById(idOfferta);
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

        // Eliminiamo l'entità con l'id passato per parametro
        offertaInversaRepository.deleteById(idOfferta);
    }

    @Override
    public void checkFieldsValid(OffertaInversaDto offertaInversaDto) throws InvalidParameterException {
        offertaDiVenditoreService.checkFieldsValid(offertaInversaDto);
        checkAstaRiferimentoValid(offertaInversaDto.getAstaRiferimentoShallow());
    }

    private void checkAstaRiferimentoValid(AstaShallowDto astaRiferimentoShallow) throws InvalidParameterException {
        if (astaRiferimentoShallow == null)
            throw new InvalidParameterException("L'asta riferimento non può essere null!");
    }

    @Override
    public void convertRelations(OffertaInversaDto offertaInversaDto, OffertaInversa offertaInversa) throws InvalidParameterException {
        offertaDiVenditoreService.convertRelations(offertaInversaDto, offertaInversa);
        convertAstaRiferimentoShallow(offertaInversaDto.getAstaRiferimentoShallow(), offertaInversa);
    }

    private void convertAstaRiferimentoShallow(AstaShallowDto astaRiferimentoShallow, OffertaInversa offertaInversa) throws IdNotFoundException, InvalidTypeException {
        Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaRiferimentoShallow);

        if (convertedAsta != null) {
            if (convertedAsta instanceof AstaInversa convertedAstaInversa) {
                offertaInversa.setAstaRiferimento(convertedAstaInversa);
                convertedAstaInversa.addOffertaRicevuta(offertaInversa);
            } else {
                throw new InvalidTypeException("Un'offerta inversa può riferirsi solo ad aste inverse!");
            }
        }
    }

    @Override
    public void updatePresentFields(OffertaInversaDto updatedOffertaInversaDto, OffertaInversa existingOffertaInversa) throws InvalidParameterException {
        offertaDiVenditoreService.updatePresentFields(updatedOffertaInversaDto, existingOffertaInversa);

        // Non è possibile modificare l'associazione "astaRiferimento" tramite la risorsa "offerte/di-venditori/inverse"
    }
}
