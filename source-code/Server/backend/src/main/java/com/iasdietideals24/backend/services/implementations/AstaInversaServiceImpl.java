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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class AstaInversaServiceImpl implements AstaInversaService {

    public static final String LOG_RECUPERO_ASTE_IN_CORSO = "Recupero le aste inverse dal database...";
    public static final String LOG_FOUND_ASTE = "foundAsteInverse: {}";
    public static final String LOG_ASTE_RECUPERATE = "Aste inverse recuperate dal database.";
    public static final String LOG_RECUPERO_ASTA = "Recupero l'asta inversa dal database...";
    public static final String LOG_FOUND_ASTA = "foundAstaInversa: {}";
    public static final String LOG_ASTA_RECUPERATA = "Asta inversa recuperata dal database.";

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

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        AstaInversa nuovaAstaInversa = astaInversaMapper.toEntity(nuovaAstaInversaDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaAstaInversa: {}", nuovaAstaInversa);

        // Recuperiamo le associazioni
        convertRelations(nuovaAstaInversaDto, nuovaAstaInversa);

        log.trace("nuovaAstaInversa: {}", nuovaAstaInversa);

        log.debug("Salvo l'asta inversa nel database...");

        // Registriamo l'entità
        AstaInversa savedAstaInversa = astaInversaRepository.save(nuovaAstaInversa);

        log.trace("savedAstaInversa: {}", savedAstaInversa);
        log.debug("Asta inversa salvata correttamente nel database con id asta {}...", savedAstaInversa.getIdAsta());

        return astaInversaMapper.toDto(savedAstaInversa);
    }

    @Override
    public Page<AstaInversaDto> findAll(Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);

        // Recuperiamo tutte le entità
        Page<AstaInversa> foundAsteInverse = astaInversaRepository.findAll(pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteInverse);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteInverse.map(astaInversaMapper::toDto);
    }

    @Override
    public Page<AstaInversaDto> findByProprietarioIdAccountIs(Long idAccount, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Id account proprietario da cercare: {}", idAccount);

        // Recuperiamo tutte le entità
        Page<AstaInversa> foundAsteInverse = astaInversaRepository.findByProprietario_IdAccountIs(idAccount, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteInverse);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteInverse.map(astaInversaMapper::toDto);
    }

    @Override
    public Page<AstaInversaDto> findByNomeLikeAndCategoriaNomeIs(String nomeAsta, String nomeCategoria, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Nome asta da cercare: {}", nomeAsta);
        log.trace("Nome categoria da cercare: {}", nomeCategoria);

        // Recuperiamo tutte le entità
        Page<AstaInversa> foundAsteInverse = astaInversaRepository.findByNomeLikeAndCategoria_NomeIs(nomeAsta, nomeCategoria, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteInverse);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteInverse.map(astaInversaMapper::toDto);
    }

    @Override
    public Page<AstaInversaDto> findByOfferenteIdAccountIs(Long idAccount, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Id account offerente da cercare: {}", idAccount);

        // Recuperiamo tutte le entità
        Page<AstaInversa> foundAsteInverse = astaInversaRepository.findByOfferente_IdAccountIs(idAccount, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteInverse);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteInverse.map(astaInversaMapper::toDto);
    }

    @Override
    public Optional<AstaInversaDto> findOne(Long idAsta) {

        log.trace("Id asta da recuperare: {}", idAsta);
        log.debug(LOG_RECUPERO_ASTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaInversa> foundAstaInversa = astaInversaRepository.findById(idAsta);

        log.trace(LOG_FOUND_ASTA, foundAstaInversa);
        log.debug(LOG_ASTA_RECUPERATA);

        return foundAstaInversa.map(astaInversaMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAsta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return astaInversaRepository.existsById(idAsta);
    }

    @Override
    public AstaInversaDto fullUpdate(Long idAsta, AstaInversaDto updatedAstaInversaDto) throws InvalidParameterException {

        log.trace("Id asta da sostituire: {}", idAsta);

        updatedAstaInversaDto.setIdAsta(idAsta);

        if (!astaInversaRepository.existsById(idAsta)) {
            log.warn("L'id asta '{}' non corrisponde a nessuna asta inversa esistente!", idAsta);

            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta inversa esistente!");
        } else {
            return this.create(updatedAstaInversaDto);
        }
    }

    @Override
    public AstaInversaDto partialUpdate(Long idAsta, AstaInversaDto updatedAstaInversaDto) throws InvalidParameterException {

        log.trace("Id asta da aggiornare: {}", idAsta);

        updatedAstaInversaDto.setIdAsta(idAsta);

        log.debug(LOG_RECUPERO_ASTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaInversa> foundAstaInversa = astaInversaRepository.findById(idAsta);

        log.trace(LOG_FOUND_ASTA, foundAstaInversa);
        log.debug(LOG_ASTA_RECUPERATA);

        if (foundAstaInversa.isEmpty())
            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta inversa esistente!");
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

        log.trace("Id asta da eliminare: {}", idAsta);
        log.debug("Elimino l'asta inversa dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        astaInversaRepository.deleteById(idAsta);

        log.debug("Asta inversa eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(AstaInversaDto astaInversaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di asta inversa...");

        astaDiCompratoreService.checkFieldsValid(astaInversaDto);
        checkSogliaInizialeValid(astaInversaDto.getSogliaIniziale());

        log.debug("Integrità dei dati di asta inversa verificata.");
    }

    private void checkSogliaInizialeValid(BigDecimal sogliaIniziale) throws InvalidParameterException {

        log.trace("Controllo che 'sogliaIniziale' sia valido...");

        if (sogliaIniziale == null)
            throw new InvalidParameterException("La soglia iniziale non può essere null!");
        else if (sogliaIniziale.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException("La soglia iniziale deve essere positiva!");

        log.trace("'sogliaIniziale' valido.");
    }

    @Override
    public void convertRelations(AstaInversaDto astaInversaDto, AstaInversa astaInversa) throws InvalidParameterException {

        log.debug("Recupero le associazioni di asta inversa...");

        astaDiCompratoreService.convertRelations(astaInversaDto, astaInversa);
        convertOfferteRicevute(astaInversaDto.getOfferteRicevuteShallow(), astaInversa);

        log.debug("Associazioni di asta inversa recuperate.");
    }

    private void convertOfferteRicevute(Set<OffertaShallowDto> offerteRicevuteShallow, AstaInversa astaInversa) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'offerteRicevute'...");

        astaInversa.getOfferteRicevute().clear();

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

        log.trace("'offerteRicevute' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AstaInversaDto updatedAstaInversaDto, AstaInversa existingAstaInversa) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di asta inversa richieste...");

        astaDiCompratoreService.updatePresentFields(updatedAstaInversaDto, existingAstaInversa);
        ifPresentUpdateSogliaIniziale(updatedAstaInversaDto.getSogliaIniziale(), existingAstaInversa);

        log.debug("Modifiche di asta inversa effettuate correttamente.");

        // Non è possibile modificare l'associazione "offerteRicevute" tramite la risorsa "aste/di-compratori/inverse"
    }

    private void ifPresentUpdateSogliaIniziale(BigDecimal updatedSogliaIniziale, AstaInversa existingAstaInversa) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'sogliaIniziale'...");

        if (updatedSogliaIniziale != null) {
            this.checkSogliaInizialeValid(updatedSogliaIniziale);
            existingAstaInversa.setSogliaIniziale(updatedSogliaIniziale);
        }

        log.trace("'sogliaIniziale' modificato correttamente.");
    }
}