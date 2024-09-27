package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.AstaTempoFissoMapper;
import com.iasdietideals24.backend.repositories.AstaTempoFissoRepository;
import com.iasdietideals24.backend.services.AstaDiVenditoreService;
import com.iasdietideals24.backend.services.AstaTempoFissoService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class AstaTempoFissoServiceImpl implements AstaTempoFissoService {

    private final AstaDiVenditoreService astaDiVenditoreService;
    private final AstaTempoFissoMapper astaTempoFissoMapper;
    private final AstaTempoFissoRepository astaTempoFissoRepository;
    private final RelationsConverter relationsConverter;

    public AstaTempoFissoServiceImpl(AstaDiVenditoreService astaDiVenditoreService,
                                     AstaTempoFissoMapper astaTempoFissoMapper,
                                     AstaTempoFissoRepository astaTempoFissoRepository,
                                     RelationsConverter relationsConverter) {
        this.astaDiVenditoreService = astaDiVenditoreService;
        this.astaTempoFissoMapper = astaTempoFissoMapper;
        this.astaTempoFissoRepository = astaTempoFissoRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public AstaTempoFissoDto create(AstaTempoFissoDto nuovaAstaTempoFissoDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaAstaTempoFissoDto);

        // Convertiamo a entità
        AstaTempoFisso nuovaAstaTempoFisso = astaTempoFissoMapper.toEntity(nuovaAstaTempoFissoDto);

        // Registriamo l'entità
        AstaTempoFisso savedAstaTempoFisso = astaTempoFissoRepository.save(nuovaAstaTempoFisso);

        return astaTempoFissoMapper.toDto(savedAstaTempoFisso);
    }

    @Override
    public Page<AstaTempoFissoDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<AstaTempoFisso> foundAsteTempoFisso = astaTempoFissoRepository.findAll(pageable);

        return foundAsteTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public Optional<AstaTempoFissoDto> findOne(Long idAsta) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaTempoFisso> foundAstaTempoFisso = astaTempoFissoRepository.findById(idAsta);

        return foundAstaTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAsta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return astaTempoFissoRepository.existsById(idAsta);
    }

    @Override
    public AstaTempoFissoDto fullUpdate(Long idAsta, AstaTempoFissoDto updatedAstaTempoFissoDto) throws InvalidParameterException {

        updatedAstaTempoFissoDto.setIdAsta(idAsta);

        if (!astaTempoFissoRepository.existsById(idAsta))
            throw new UpdateRuntimeException("L'id asta \"" + idAsta + "\" non corrisponde a nessuna asta a tempo fisso!");
        else {
            return this.create(updatedAstaTempoFissoDto);
        }
    }

    @Override
    public AstaTempoFissoDto partialUpdate(Long idAsta, AstaTempoFissoDto updatedAstaTempoFissoDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedAstaTempoFissoDto.setIdAsta(idAsta);

        Optional<AstaTempoFisso> foundAstaTempoFisso = astaTempoFissoRepository.findById(idAsta);
        if (foundAstaTempoFisso.isEmpty())
            throw new UpdateRuntimeException("L'id asta non corrisponde a nessuna asta a tempo fisso esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            AstaTempoFisso existingAstaTempoFisso = foundAstaTempoFisso.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedAstaTempoFissoDto, existingAstaTempoFisso);

            return astaTempoFissoMapper.toDto(astaTempoFissoRepository.save(existingAstaTempoFisso));
        }
    }

    @Override
    public void delete(Long idAsta) {

        // Eliminiamo l'entità con l'id passato per parametro
        astaTempoFissoRepository.deleteById(idAsta);
    }

    @Override
    public void checkFieldsValid(AstaTempoFissoDto astaTempoFissoDto) throws InvalidParameterException {
        astaDiVenditoreService.checkFieldsValid(astaTempoFissoDto);
        checkSogliaMinimaValid(astaTempoFissoDto.getSogliaMinima());
    }

    private void checkSogliaMinimaValid(BigDecimal sogliaMinima) throws InvalidParameterException {
        if (sogliaMinima == null)
            throw new InvalidParameterException("La soglia minima non può essere null!");
        else if (sogliaMinima.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException("La soglia minima deve essere positiva!");
    }

    @Override
    public void convertRelations(AstaTempoFissoDto astaTempoFissoDto, AstaTempoFisso astaTempoFisso) throws InvalidParameterException {
        astaDiVenditoreService.convertRelations(astaTempoFissoDto, astaTempoFisso);
        convertOfferteRicevute(astaTempoFissoDto.getOfferteRicevuteShallow(), astaTempoFisso);
    }

    private void convertOfferteRicevute(Set<OffertaShallowDto> offerteRicevuteShallow, AstaTempoFisso astaTempoFisso) throws IdNotFoundException, InvalidTypeException {
        if (offerteRicevuteShallow != null) {
            for (OffertaShallowDto offertaShallowDto : offerteRicevuteShallow) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta instanceof OffertaTempoFisso convertedOffertaTempoFisso) {
                    astaTempoFisso.addOffertaRicevuta(convertedOffertaTempoFisso);
                    convertedOffertaTempoFisso.setAstaRiferimento(astaTempoFisso);
                } else {
                    throw new InvalidTypeException("Un'asta a tempo fisso può ricevere solo offerte a tempo fisso!");
                }
            }
        }
    }

    @Override
    public void updatePresentFields(AstaTempoFissoDto updatedAstaTempoFissoDto, AstaTempoFisso existingAstaTempoFisso) throws InvalidParameterException {
        astaDiVenditoreService.updatePresentFields(updatedAstaTempoFissoDto, existingAstaTempoFisso);
        ifPresentUpdateSogliaMinima(updatedAstaTempoFissoDto.getSogliaMinima(), existingAstaTempoFisso);

        // Non è possibile modificare l'associazione "offerteRicevute" tramite la risorsa "aste/di-venditori/tempo-fisso"
    }

    private void ifPresentUpdateSogliaMinima(BigDecimal updatedSogliaMinima, AstaTempoFisso existingAstaTempoFisso) throws InvalidParameterException {
        if (updatedSogliaMinima != null) {
            this.checkSogliaMinimaValid(updatedSogliaMinima);
            existingAstaTempoFisso.setSogliaMinima(updatedSogliaMinima);
        }
    }
}
