package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.StatoAsta;
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
import com.iasdietideals24.backend.services.helper.BuildNotice;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class AstaTempoFissoServiceImpl implements AstaTempoFissoService {

    public static final String LOG_RECUPERO_ASTE_IN_CORSO = "Recupero le aste a tempo fisso dal database...";
    public static final String LOG_FOUND_ASTE = "foundAsteTempoFisso: {}";
    public static final String LOG_ASTE_RECUPERATE = "Aste a tempo fisso recuperate dal database.";
    public static final String LOG_RECUPERO_ASTA = "Recupero l'asta a tempo fisso dal database...";
    public static final String LOG_FOUND_ASTA = "foundAstaTempoFisso: {}";
    public static final String LOG_ASTA_RECUPERATA = "Asta a tempo fisso recuperata dal database.";

    private final AstaDiVenditoreService astaDiVenditoreService;

    private final AstaTempoFissoMapper astaTempoFissoMapper;
    private final AstaTempoFissoRepository astaTempoFissoRepository;

    private final RelationsConverter relationsConverter;
    private final BuildNotice buildNotice;

    public AstaTempoFissoServiceImpl(AstaDiVenditoreService astaDiVenditoreService,
                                     AstaTempoFissoMapper astaTempoFissoMapper,
                                     AstaTempoFissoRepository astaTempoFissoRepository,
                                     RelationsConverter relationsConverter,
                                     BuildNotice buildNotice) {
        this.astaDiVenditoreService = astaDiVenditoreService;
        this.astaTempoFissoMapper = astaTempoFissoMapper;
        this.astaTempoFissoRepository = astaTempoFissoRepository;
        this.relationsConverter = relationsConverter;
        this.buildNotice = buildNotice;
    }

    @Override
    public AstaTempoFissoDto create(AstaTempoFissoDto nuovaAstaTempoFissoDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovaAstaTempoFissoDto);

        log.debug("Modifiche di asta a tempo fisso effettuate correttamente.");

        // Convertiamo a entità
        AstaTempoFisso nuovaAstaTempoFisso = astaTempoFissoMapper.toEntity(nuovaAstaTempoFissoDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaAstaTempoFisso: {}", nuovaAstaTempoFisso);

        // Recuperiamo le associazioni
        convertRelations(nuovaAstaTempoFissoDto, nuovaAstaTempoFisso);

        log.trace("nuovaAstaTempoFisso: {}", nuovaAstaTempoFisso);

        log.debug("Salvo l'asta a tempo fisso nel database...");

        // Registriamo l'entità
        AstaTempoFisso savedAstaTempoFisso = astaTempoFissoRepository.save(nuovaAstaTempoFisso);

        log.trace("savedAstaTempoFisso: {}", savedAstaTempoFisso);
        log.debug("Asta a tempo fisso salvata correttamente nel database con id asta {}...", savedAstaTempoFisso.getIdAsta());

        return astaTempoFissoMapper.toDto(savedAstaTempoFisso);
    }

    @Override
    public Page<AstaTempoFissoDto> findAll(Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);

        // Recuperiamo tutte le entità
        Page<AstaTempoFisso> foundAsteTempoFisso = astaTempoFissoRepository.findAll(pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteTempoFisso);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public Page<AstaTempoFissoDto> findByProprietarioIdAccountIs(Long idAccount, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Id account proprietario da cercare: {}", idAccount);

        // Recuperiamo tutte le entità
        Page<AstaTempoFisso> foundAsteTempoFisso = astaTempoFissoRepository.findByProprietario_IdAccountIs(idAccount, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteTempoFisso);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public Page<AstaTempoFissoDto> findByProprietarioIdAccountNot(Long idAccount, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Id account proprietario da escludere: {}", idAccount);

        // Recuperiamo tutte le entità
        Page<AstaTempoFisso> foundAsteTempoFisso = astaTempoFissoRepository.findByProprietario_IdAccountNot(idAccount, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteTempoFisso);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public Page<AstaTempoFissoDto> findByNomeLikeAndCategoriaNomeIs(String nomeAsta, String nomeCategoria, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Nome asta da cercare: {}", nomeAsta);
        log.trace("Nome categoria da cercare: {}", nomeCategoria);

        // Recuperiamo tutte le entità
        Page<AstaTempoFisso> foundAsteTempoFisso = astaTempoFissoRepository.findByNomeLikeAndCategoria_NomeIs(nomeAsta, nomeCategoria, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteTempoFisso);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public Page<AstaTempoFissoDto> findByOfferenteIdAccountIs(Long idAccount, Pageable pageable) {

        log.debug(LOG_RECUPERO_ASTE_IN_CORSO);
        log.trace("Id account offerente da cercare: {}", idAccount);

        // Recuperiamo tutte le entità
        Page<AstaTempoFisso> foundAsteTempoFisso = astaTempoFissoRepository.findByOfferente_IdAccountIs(idAccount, pageable);

        log.trace(LOG_FOUND_ASTE, foundAsteTempoFisso);
        log.debug(LOG_ASTE_RECUPERATE);

        return foundAsteTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public Optional<AstaTempoFissoDto> findOne(Long idAsta) {

        log.trace("Id asta da recuperare: {}", idAsta);
        log.debug(LOG_RECUPERO_ASTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaTempoFisso> foundAstaTempoFisso = astaTempoFissoRepository.findById(idAsta);

        log.trace(LOG_FOUND_ASTA, foundAstaTempoFisso);
        log.debug(LOG_ASTA_RECUPERATA);

        return foundAstaTempoFisso.map(astaTempoFissoMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAsta) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return astaTempoFissoRepository.existsById(idAsta);
    }

    @Override
    public AstaTempoFissoDto fullUpdate(Long idAsta, AstaTempoFissoDto updatedAstaTempoFissoDto) throws InvalidParameterException {

        log.trace("Id asta da sostituire: {}", idAsta);

        updatedAstaTempoFissoDto.setIdAsta(idAsta);

        if (!astaTempoFissoRepository.existsById(idAsta)) {
            log.warn("L'id asta '{}' non corrisponde a nessuna asta a tempo fisso esistente!", idAsta);

            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta a tempo fisso esistente!");
        } else {
            return this.create(updatedAstaTempoFissoDto);
        }
    }

    @Override
    public AstaTempoFissoDto partialUpdate(Long idAsta, AstaTempoFissoDto updatedAstaTempoFissoDto) throws InvalidParameterException {

        log.trace("Id asta da aggiornare: {}", idAsta);

        updatedAstaTempoFissoDto.setIdAsta(idAsta);

        log.debug(LOG_RECUPERO_ASTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<AstaTempoFisso> foundAstaTempoFisso = astaTempoFissoRepository.findById(idAsta);

        log.trace(LOG_FOUND_ASTA, foundAstaTempoFisso);
        log.debug(LOG_ASTA_RECUPERATA);

        if (foundAstaTempoFisso.isEmpty())
            throw new UpdateRuntimeException("L'id asta '" + idAsta + "' non corrisponde a nessuna asta a tempo fisso esistente!");
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

        log.trace("Id asta da eliminare: {}", idAsta);
        log.debug("Elimino l'asta a tempo fisso dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        astaTempoFissoRepository.deleteById(idAsta);

        log.debug("Asta a tempo fisso eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(AstaTempoFissoDto astaTempoFissoDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di asta a tempo fisso...");

        astaDiVenditoreService.checkFieldsValid(astaTempoFissoDto);
        checkSogliaMinimaValid(astaTempoFissoDto.getSogliaMinima());

        log.debug("Integrità dei dati di asta a tempo fisso verificata.");
    }

    private void checkSogliaMinimaValid(BigDecimal sogliaMinima) throws InvalidParameterException {

        log.trace("Controllo che 'sogliaMinima' sia valido...");

        if (sogliaMinima == null)
            throw new InvalidParameterException("La soglia minima non può essere null!");
        else if (sogliaMinima.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException("La soglia minima deve essere positiva!");

        log.trace("'sogliaMinima' valido.");
    }

    @Override
    public void convertRelations(AstaTempoFissoDto astaTempoFissoDto, AstaTempoFisso astaTempoFisso) throws InvalidParameterException {

        log.debug("Recupero le associazioni di asta a tempo fisso...");

        astaDiVenditoreService.convertRelations(astaTempoFissoDto, astaTempoFisso);
        convertOfferteRicevute(astaTempoFissoDto.getOfferteRicevuteShallow(), astaTempoFisso);

        log.debug("Associazioni di asta a tempo fisso recuperate.");
    }

    private void convertOfferteRicevute(Set<OffertaShallowDto> offerteRicevuteShallow, AstaTempoFisso astaTempoFisso) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'offerteRicevute'...");

        astaTempoFisso.getOfferteRicevute().clear();

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

        log.trace("'offerteRicevute' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AstaTempoFissoDto updatedAstaTempoFissoDto, AstaTempoFisso existingAstaTempoFisso) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di asta a tempo fisso richieste...");

        astaDiVenditoreService.updatePresentFields(updatedAstaTempoFissoDto, existingAstaTempoFisso);
        ifPresentUpdateSogliaMinima(updatedAstaTempoFissoDto.getSogliaMinima(), existingAstaTempoFisso);

        log.debug("Modifiche di asta a tempo fisso effettuate correttamente.");

        // Non è possibile modificare l'associazione "offerteRicevute" tramite la risorsa "aste/di-venditori/tempo-fisso"
    }

    private void ifPresentUpdateSogliaMinima(BigDecimal updatedSogliaMinima, AstaTempoFisso existingAstaTempoFisso) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'sogliaMinima'...");

        if (updatedSogliaMinima != null) {
            this.checkSogliaMinimaValid(updatedSogliaMinima);
            existingAstaTempoFisso.setSogliaMinima(updatedSogliaMinima);
        }

        log.trace("'sogliaMinima' modificato correttamente.");
    }

    @Override
    public void closeAstaTempoFisso(AstaTempoFisso expiredAsta) {

        log.debug("L'asta con id '{}' è scaduta. Chiudo l'asta...", expiredAsta.getIdAsta());

        expiredAsta.setStato(StatoAsta.CLOSED);

        log.debug("Asta chiusa. Determino il vincitore...");

        Set<OffertaTempoFisso> offerteRicevute = expiredAsta.getOfferteRicevute();

        if (!offerteRicevute.isEmpty()) {

            OffertaTempoFisso mostValueOffertaRicevuta = offerteRicevute.iterator().next();
            for (OffertaTempoFisso offertaRicevuta : offerteRicevute) {
                if (offertaRicevuta.getValore().compareTo(mostValueOffertaRicevuta.getValore()) > 0)
                    mostValueOffertaRicevuta = offertaRicevuta;
            }

            log.trace("mostValueOffertaRicevuta: {}", mostValueOffertaRicevuta);

            if (mostValueOffertaRicevuta.getValore().compareTo(expiredAsta.getSogliaMinima()) >= 0) {

                log.debug("Vincitore trovato. Invio la notifica...");

                buildNotice.notifyOffertaTempoFissoVincitrice(mostValueOffertaRicevuta);

                log.debug("Notifica inviata con successo. Invio la notifica a tutti gli altri partecipanti...");

                Set<OffertaTempoFisso> offertePerdenti = new HashSet<>(offerteRicevute);
                offertePerdenti.remove(mostValueOffertaRicevuta);

                log.trace("offerteRicevute: {}", offerteRicevute);
                log.trace("offertePerdenti: {}", offertePerdenti);

                buildNotice.notifyOffertaTempoFissoPerdente(offertePerdenti);

                log.debug("Notifica inviata con successo.");
            } else {
                log.debug("Non è stata raggiunta la soglia minima.");
            }
        } else {
            log.debug("Non ci sono offerte all'asta.");
        }

        log.debug("Invio la notifica al proprietario...");

        buildNotice.notifyAstaTempoFissoScaduta(expiredAsta);

        log.debug("Notifica inviata con successo.");
    }
}
