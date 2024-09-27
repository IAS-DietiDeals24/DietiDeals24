package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.AstaInversaMapper;
import com.iasdietideals24.backend.repositories.AstaInversaRepository;
import com.iasdietideals24.backend.services.AstaDiCompratoreService;
import com.iasdietideals24.backend.services.AstaInversaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class AstaInversaServiceImpl implements AstaInversaService {

    private final AstaDiCompratoreService astaDiCompratoreService;
    private final AstaInversaMapper astaInversaMapper;
    private final AstaInversaRepository astaInversaRepository;
    private final RelationsConverter relationsConverter;

    public AstaInversaServiceImpl(AstaDiCompratoreService astaDiCompratoreService,
                                  AstaInversaMapper astaInversaMapper,
                                  AstaInversaRepository astaInversaRepository,
                                  RelationsConverter relationsConverter) {
        this.astaDiCompratoreService = astaDiCompratoreService;
        this.astaInversaMapper = astaInversaMapper;
        this.astaInversaRepository = astaInversaRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public AstaInversaDto create(AstaInversaDto nuovaAstaInversaDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaAstaInversaDto);

        // Convertiamo a entità
        AstaInversa nuovaAstaInversa = astaInversaMapper.toEntity(nuovaAstaInversaDto);

        // Registriamo l'entità
        AstaInversa savedAstaInversa = astaInversaRepository.save(nuovaAstaInversa);

        return astaInversaMapper.toDto(savedAstaInversa);
    }

    @Override
    public Page<AstaInversaDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<AstaInversa> foundAsteInverse = astaInversaRepository.findAll(pageable);

        return foundAsteInverse.map(astaInversaMapper::toDto);
    }

    @Override
    public Optional<AstaInversaDto> findOne(Long idAsta) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaInversa> foundAstaInversa = astaInversaRepository.findById(idAsta);

        return foundAstaInversa.map(astaInversaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAsta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return astaInversaRepository.existsById(idAsta);
    }

    @Override
    public AstaInversaDto fullUpdate(Long idAsta, AstaInversaDto updatedAstaInversaDto) throws InvalidParameterException {

        updatedAstaInversaDto.setIdAsta(idAsta);

        if (!astaInversaRepository.existsById(idAsta))
            throw new UpdateRuntimeException("L'id asta \"" + idAsta + "\" non corrisponde a nessuna asta inversa!");
        else {
            return this.create(updatedAstaInversaDto);
        }
    }

    @Override
    public AstaInversaDto partialUpdate(Long idAsta, AstaInversaDto updatedAstaInversaDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedAstaInversaDto.setIdAsta(idAsta);

        Optional<AstaInversa> foundAstaInversa = astaInversaRepository.findById(idAsta);
        if (foundAstaInversa.isEmpty())
            throw new UpdateRuntimeException("L'id asta non corrisponde a nessuna asta inversa esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            AstaInversa existingAstaInversa = foundAstaInversa.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedAstaInversaDto, existingAstaInversa);

            return astaInversaMapper.toDto(astaInversaRepository.save(existingAstaInversa));
        }
    }

    @Override
    public void delete(Long idAsta) {

        // Eliminiamo l'entità con l'id passato per parametro
        astaInversaRepository.deleteById(idAsta);
    }

    @Override
    public void checkFieldsValid(AstaInversaDto astaInversaDto) throws InvalidParameterException {
        astaDiCompratoreService.checkFieldsValid(astaInversaDto);
        checkSogliaInizialeValid(astaInversaDto.getSogliaIniziale());
    }

    private void checkSogliaInizialeValid(BigDecimal sogliaIniziale) throws InvalidParameterException {
        if (sogliaIniziale == null)
            throw new InvalidParameterException("La soglia iniziale non può essere null!");
        else if (sogliaIniziale.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException("La soglia iniziale deve essere positiva!");
    }

    @Override
    public void convertRelations(AstaInversaDto astaInversaDto, AstaInversa astaInversa) throws InvalidParameterException {
        astaDiCompratoreService.convertRelations(astaInversaDto, astaInversa);
        convertOfferteRicevute(astaInversaDto.getOfferteRicevuteShallow(), astaInversa);
    }

    private void convertOfferteRicevute(Set<OffertaShallowDto> offerteRicevuteShallow, AstaInversa astaInversa) throws IdNotFoundException, InvalidTypeException {
        if (offerteRicevuteShallow != null) {
            for (OffertaShallowDto offertaShallowDto : offerteRicevuteShallow) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta instanceof OffertaInversa convertedOffertaInversa) {
                    astaInversa.addOffertaRicevuta(convertedOffertaInversa);
                    convertedOffertaInversa.setAstaRiferimento(astaInversa);
                } else {
                    throw new InvalidTypeException("Un'asta inversa può ricevere solo offerte inverse!");
                }
            }
        }
    }

    @Override
    public void updatePresentFields(AstaInversaDto updatedAstaInversaDto, AstaInversa existingAstaInversa) throws InvalidParameterException {
        astaDiCompratoreService.updatePresentFields(updatedAstaInversaDto, existingAstaInversa);
        ifPresentUpdateSogliaIniziale(updatedAstaInversaDto.getSogliaIniziale(), existingAstaInversa);

        // Non è possibile modificare l'associazione "offerteRicevute" tramite la risorsa "aste/di-compratori/inverse"
    }

    private void ifPresentUpdateSogliaIniziale(BigDecimal updatedSogliaIniziale, AstaInversa existingAstaInversa) throws InvalidParameterException {
        if (updatedSogliaIniziale != null) {
            this.checkSogliaInizialeValid(updatedSogliaIniziale);
            existingAstaInversa.setSogliaIniziale(updatedSogliaIniziale);
        }
    }
}
