package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.VenditoreMapper;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.VenditoreService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class VenditoreServiceImpl implements VenditoreService {

    public static final String LOG_RECUPERO_ACCOUNT_VENDITORE = "Recupero l'account venditore dal database...";
    public static final String LOG_FOUND_VENDITORE = "foundVenditore: {}";
    public static final String LOG_ACCOUNT_VENDITORE_RECUPERATO = "Account venditore recuperato dal database.";

    private final AccountService accountService;
    private final VenditoreMapper venditoreMapper;
    private final VenditoreRepository venditoreRepository;
    private final RelationsConverter relationsConverter;

    public VenditoreServiceImpl(AccountService accountService, VenditoreMapper venditoreMapper, VenditoreRepository venditoreRepository, RelationsConverter relationsConverter) {
        this.accountService = accountService;
        this.venditoreMapper = venditoreMapper;
        this.venditoreRepository = venditoreRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public VenditoreDto create(VenditoreDto nuovoVenditoreDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovoVenditoreDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        Venditore nuovoVenditore = venditoreMapper.toEntity(nuovoVenditoreDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovoVenditore: {}", nuovoVenditore);

        // Recuperiamo le associazioni
        convertRelations(nuovoVenditoreDto, nuovoVenditore);

        log.trace("nuovoVenditore: {}", nuovoVenditore);

        log.debug("Salvo l'account venditore nel database...");

        // Registriamo l'entità
        Venditore savedVenditore = venditoreRepository.save(nuovoVenditore);

        log.trace("savedVenditore: {}", savedVenditore);
        log.debug("Account venditore salvato correttamente nel database con id account {}...", savedVenditore.getIdAccount());

        return venditoreMapper.toDto(savedVenditore);
    }

    @Override
    public Page<VenditoreDto> findAll(Pageable pageable) {

        log.debug("Recupero gli accounts venditori dal database...");

        // Recuperiamo tutte le entità
        Page<Venditore> foundVenditori = venditoreRepository.findAll(pageable);

        log.trace("foundVenditori: {}", foundVenditori);
        log.debug("Accounts venditori recuperati dal database.");

        return foundVenditori.map(venditoreMapper::toDto);
    }

    @Override
    public Optional<VenditoreDto> findOne(Long idAccount) {

        log.trace("Id account da recuperare: {}", idAccount);
        log.debug(LOG_RECUPERO_ACCOUNT_VENDITORE);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Venditore> foundVenditore = venditoreRepository.findById(idAccount);

        log.trace(LOG_FOUND_VENDITORE, foundVenditore);
        log.debug(LOG_ACCOUNT_VENDITORE_RECUPERATO);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public Page<VenditoreDto> findByEmail(String email, Pageable pageable) {

        log.trace("Email account da recuperare: {}", email);
        log.debug(LOG_RECUPERO_ACCOUNT_VENDITORE);

        // Recuperiamo l'entità con l'id passato per parametro
        Page<Venditore> foundVenditore = venditoreRepository.findByEmail(email, pageable);

        log.trace(LOG_FOUND_VENDITORE, foundVenditore);
        log.debug(LOG_ACCOUNT_VENDITORE_RECUPERATO);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAccount) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return venditoreRepository.existsById(idAccount);
    }

    @Override
    public VenditoreDto fullUpdate(Long idAccount, VenditoreDto updatedVenditoreDto) throws InvalidParameterException {

        log.trace("Id account da sostituire: {}", idAccount);

        updatedVenditoreDto.setIdAccount(idAccount);

        if (!venditoreRepository.existsById(idAccount))
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun account esistente!");
        else {
            // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
            return this.create(updatedVenditoreDto);
        }
    }

    @Override
    public VenditoreDto partialUpdate(Long idAccount, VenditoreDto updatedVenditoreDto) throws InvalidParameterException {

        log.trace("Id account da aggiornare: {}", idAccount);

        updatedVenditoreDto.setIdAccount(idAccount);

        log.debug(LOG_RECUPERO_ACCOUNT_VENDITORE);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Venditore> foundVenditore = venditoreRepository.findById(idAccount);

        log.trace(LOG_FOUND_VENDITORE, foundVenditore);
        log.debug(LOG_ACCOUNT_VENDITORE_RECUPERATO);

        if (foundVenditore.isEmpty())
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun venditore esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            Venditore existingVenditore = foundVenditore.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedVenditoreDto, existingVenditore);

            return venditoreMapper.toDto(venditoreRepository.save(existingVenditore));
        }
    }

    @Override
    public void delete(Long idAccount) throws InvalidParameterException {

        log.trace("Id account da eliminare: {}", idAccount);
        log.debug("Elimino l'account venditore dal database...");

        log.debug("Verifico che l'account non sia l'ultimo account del profilo al quale è associato...");

        Optional<Venditore> existingVenditore = venditoreRepository.findById(idAccount);
        if (existingVenditore.isPresent() && accountService.isLastAccountOfProfilo(existingVenditore.get())) {
            log.warn("Non puoi eliminare l'unico account associato al profilo!");

            throw new IllegalDeleteRequestException("Non puoi eliminare l'unico account associato al profilo!");
        }

        log.debug("L'account non è l'ultimo account del profilo al quale è associato. Elimino l'account venditore dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        venditoreRepository.deleteById(idAccount);

        log.debug("Account venditore eliminato dal database.");
    }

    @Override
    public void checkFieldsValid(VenditoreDto venditoreDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di account venditore...");

        accountService.checkFieldsValid(venditoreDto);

        log.debug("Integrità dei dati di account venditore verificata.");
    }

    @Override
    public void convertRelations(VenditoreDto venditoreDto, Venditore venditore) throws InvalidParameterException {

        log.debug("Recupero le associazioni di account venditore...");

        accountService.convertRelations(venditoreDto, venditore);
        convertAstePosseduteShallow(venditoreDto.getAstePosseduteShallow(), venditore);
        convertOfferteCollegateShallow(venditoreDto.getOfferteCollegateShallow(), venditore);

        log.debug("Associazioni di account venditore recuperate.");
    }

    private void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Venditore venditore) throws InvalidParameterException {
        if (astePosseduteShallowDto != null) {
            for (AstaShallowDto astaShallowDto : astePosseduteShallowDto) {

                Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaShallowDto);

                if (convertedAsta != null) {
                    if (convertedAsta instanceof AstaDiVenditore convertedAstaDiVenditore) {
                        venditore.addAstaPosseduta(convertedAstaDiVenditore);
                        convertedAstaDiVenditore.setProprietario(venditore);
                    } else {
                        throw new InvalidTypeException("Un venditore può possedere solo aste di venditori!");
                    }
                }
            }
        }
    }

    private void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Venditore venditore) throws InvalidParameterException {

        log.trace("Converto l'associazione 'offerteCollegate'...");

        if (offerteCollegateShallowDto != null) {
            for (OffertaShallowDto offertaShallowDto : offerteCollegateShallowDto) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta != null) {
                    if (convertedOfferta instanceof OffertaDiVenditore convertedOffertaDiVenditore) {
                        venditore.addOffertaCollegata(convertedOffertaDiVenditore);
                        convertedOffertaDiVenditore.setVenditoreCollegato(venditore);
                    } else {
                        throw new InvalidTypeException("Un venditore può essere collegato solo ad offerte di venditori!");
                    }
                }
            }
        }

        log.trace("'offerteCollegate' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(VenditoreDto updatedVenditoreDto, Venditore existingVenditore) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di account venditore richieste...");

        accountService.updatePresentFields(updatedVenditoreDto, existingVenditore);

        log.debug("Modifiche di account venditore effettuate correttamente.");

        // Non è possibile modificare le associazioni "astePossedute", "offerteCollegate" tramite la risorsa "accounts/venditori"
    }
}