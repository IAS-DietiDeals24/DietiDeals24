package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CompratoreMapper;
import com.iasdietideals24.backend.repositories.CompratoreRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.CompratoreService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CompratoreServiceImpl implements CompratoreService {

    public static final String LOG_RECUPERO_ACCOUNT = "Recupero l'account compratore dal database...";
    public static final String LOG_FOUND_ACCOUNT = "foundCompratore: {}";
    public static final String LOG_ACCOUNT_RECUPERATO = "Account compratore recuperato dal database.";

    private final AccountService accountService;
    private final CompratoreMapper compratoreMapper;
    private final CompratoreRepository compratoreRepository;
    private final RelationsConverter relationsConverter;

    public CompratoreServiceImpl(AccountService accountService, CompratoreMapper compratoreMapper, CompratoreRepository compratoreRepository, RelationsConverter relationsConverter) {
        this.accountService = accountService;
        this.compratoreMapper = compratoreMapper;
        this.compratoreRepository = compratoreRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public CompratoreDto create(CompratoreDto nuovoCompratoreDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovoCompratoreDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        Compratore nuovoCompratore = compratoreMapper.toEntity(nuovoCompratoreDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovoCompratore: {}", nuovoCompratore);

        // Recuperiamo le associazioni
        convertRelations(nuovoCompratoreDto, nuovoCompratore);

        log.trace("nuovoCompratore: {}", nuovoCompratore);

        log.debug("Salvo l'account compratore nel database...");

        // Registriamo l'entità
        Compratore savedCompratore = compratoreRepository.save(nuovoCompratore);

        log.trace("savedCompratore: {}", savedCompratore);
        log.debug("Account compratore salvato correttamente nel database con id account {}...", savedCompratore.getIdAccount());

        return compratoreMapper.toDto(savedCompratore);
    }

    @Override
    public Page<CompratoreDto> findAll(Pageable pageable) {

        log.debug("Recupero gli accounts compratori dal database...");

        // Recuperiamo tutte le entità
        Page<Compratore> foundCompratori = compratoreRepository.findAll(pageable);

        log.trace("foundCompratori: {}", foundCompratori);
        log.debug("Accounts compratori recuperati dal database.");

        return foundCompratori.map(compratoreMapper::toDto);
    }

    @Override
    public Optional<CompratoreDto> findOne(Long idAccount) {

        log.debug(LOG_RECUPERO_ACCOUNT);
        log.trace("Id account da recuperare: {}", idAccount);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Compratore> foundCompratore = compratoreRepository.findById(idAccount);

        log.trace(LOG_FOUND_ACCOUNT, foundCompratore);
        log.debug(LOG_ACCOUNT_RECUPERATO);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public Page<CompratoreDto> findByEmailIs(String email, Pageable pageable) {

        log.trace("Email account da recuperare: {}", email);
        log.debug(LOG_RECUPERO_ACCOUNT);

        // Recuperiamo l'entità con l'id passato per parametro
        Page<Compratore> foundCompratore = compratoreRepository.findByEmailIs(email, pageable);

        log.trace(LOG_FOUND_ACCOUNT, foundCompratore);
        log.debug(LOG_ACCOUNT_RECUPERATO);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAccount) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return compratoreRepository.existsById(idAccount);
    }

    @Override
    public CompratoreDto fullUpdate(Long idAccount, CompratoreDto updatedCompratoreDto) throws InvalidParameterException {

        log.trace("Id account da sostituire: {}", idAccount);

        updatedCompratoreDto.setIdAccount(idAccount);

        if (!compratoreRepository.existsById(idAccount)) {
            log.warn("L'id account '{}' non corrisponde a nessun account esistente!", idAccount);

            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun account esistente!");
        } else {
            // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
            return this.create(updatedCompratoreDto);
        }
    }

    @Override
    public CompratoreDto partialUpdate(Long idAccount, CompratoreDto updatedCompratoreDto) throws InvalidParameterException {

        log.trace("Id account da aggiornare: {}", idAccount);

        updatedCompratoreDto.setIdAccount(idAccount);

        log.debug(LOG_RECUPERO_ACCOUNT);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Compratore> foundCompratore = compratoreRepository.findById(idAccount);

        log.trace(LOG_FOUND_ACCOUNT, foundCompratore);
        log.debug(LOG_ACCOUNT_RECUPERATO);

        if (foundCompratore.isEmpty())
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun compratore esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            Compratore existingCompratore = foundCompratore.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedCompratoreDto, existingCompratore);

            return compratoreMapper.toDto(compratoreRepository.save(existingCompratore));
        }
    }

    @Override
    public void delete(Long idAccount) throws InvalidParameterException {

        log.trace("Id account da eliminare: {}", idAccount);
        log.debug("Elimino l'account compratore dal database...");

        log.debug("Verifico che l'account non sia l'ultimo account del profilo al quale è associato...");

        Optional<Compratore> existingCompratore = compratoreRepository.findById(idAccount);
        if (existingCompratore.isPresent() && accountService.isLastAccountOfProfilo(existingCompratore.get())) {
            log.warn("Non puoi eliminare l'unico account associato al profilo!");

            throw new IllegalDeleteRequestException("Non puoi eliminare l'unico account associato al profilo!");
        }

        log.debug("L'account non è l'ultimo account del profilo al quale è associato. Elimino l'account compratore dal database...");
        
        // Eliminiamo l'entità con l'id passato per parametro
        compratoreRepository.deleteById(idAccount);

        log.debug("Account compratore eliminato dal database.");
    }

    @Override
    public void checkFieldsValid(CompratoreDto compratoreDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di account compratore...");

        accountService.checkFieldsValid(compratoreDto);

        log.debug("Integrità dei dati di account compratore verificata.");
    }

    @Override
    public void convertRelations(CompratoreDto compratoreDto, Compratore compratore) throws InvalidParameterException {

        log.debug("Recupero le associazioni di account compratore...");

        accountService.convertRelations(compratoreDto, compratore);
        convertAstePosseduteShallow(compratoreDto.getAstePosseduteShallow(), compratore);
        convertOfferteCollegateShallow(compratoreDto.getOfferteCollegateShallow(), compratore);

        log.debug("Associazioni di account compratore recuperate.");
    }

    private void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Compratore compratore) throws InvalidParameterException {

        log.trace("Converto l'associazione 'astePossedute'...");

        compratore.getAstePossedute().clear();

        if (astePosseduteShallowDto != null) {
            for (AstaShallowDto astaShallowDto : astePosseduteShallowDto) {

                Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaShallowDto);

                if (convertedAsta != null) {
                    if (convertedAsta instanceof AstaDiCompratore convertedAstaDiCompratore) {
                        compratore.addAstaPosseduta(convertedAstaDiCompratore);
                        convertedAstaDiCompratore.setProprietario(compratore);
                    } else {
                        throw new InvalidTypeException("Un compratore può possedere solo aste di compratori!");
                    }
                }
            }
        }

        log.trace("'astePossedute' convertita correttamente.");
    }

    private void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Compratore compratore) throws InvalidParameterException {

        log.trace("Converto l'associazione 'offerteCollegate'...");

        compratore.getOfferteCollegate().clear();

        if (offerteCollegateShallowDto != null) {
            for (OffertaShallowDto offertaShallowDto : offerteCollegateShallowDto) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta != null) {
                    if (convertedOfferta instanceof OffertaDiCompratore convertedOffertaDiCompratore) {
                        compratore.addOffertaCollegata(convertedOffertaDiCompratore);
                        convertedOffertaDiCompratore.setCompratoreCollegato(compratore);
                    } else {
                        throw new InvalidTypeException("Un compratore può essere collegato solo ad offerte di compratori!");
                    }
                }
            }
        }

        log.trace("'offerteCollegate' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(CompratoreDto updatedCompratoreDto, Compratore existingCompratore) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di account compratore richieste...");

        accountService.updatePresentFields(updatedCompratoreDto, existingCompratore);

        log.debug("Modifiche di account compratore effettuate correttamente.");

        // Non è possibile modificare le associazioni "astePossedute", "offerteCollegate" tramite la risorsa "accounts/compratori"
    }
}