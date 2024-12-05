package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.LinksProfiloDto;
import com.iasdietideals24.backend.mapstruct.mappers.AnagraficaProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.LinksProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.ProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.PutProfiloMapper;
import com.iasdietideals24.backend.repositories.AccountRepository;
import com.iasdietideals24.backend.repositories.ProfiloRepository;
import com.iasdietideals24.backend.services.ProfiloService;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ProfiloServiceImpl implements ProfiloService {

    public static final String LOG_RECUPERO_PROFILO = "Recupero il profilo dal database...";
    public static final String LOG_FOUND_PROFILO = "foundProfilo: {}";
    public static final String LOG_PROFILO_RECUPERATO = "Profilo recuperato dal database.";

    private final ProfiloMapper profiloMapper;
    private final PutProfiloMapper putProfiloMapper;
    private final AnagraficaProfiloMapper anagraficaProfiloMapper;
    private final LinksProfiloMapper linksProfiloMapper;
    private final ProfiloRepository profiloRepository;
    private final AccountRepository accountRepository;
    private final RelationsConverter relationsConverter;

    public ProfiloServiceImpl(ProfiloMapper profiloMapper,
                              PutProfiloMapper putProfiloMapper,
                              AnagraficaProfiloMapper anagraficaProfiloMapper,
                              LinksProfiloMapper linksProfiloMapper,
                              ProfiloRepository profiloRepository,
                              AccountRepository accountRepository,
                              RelationsConverter relationsConverter) {
        this.profiloMapper = profiloMapper;
        this.putProfiloMapper = putProfiloMapper;
        this.anagraficaProfiloMapper = anagraficaProfiloMapper;
        this.linksProfiloMapper = linksProfiloMapper;
        this.profiloRepository = profiloRepository;
        this.accountRepository = accountRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public ProfiloDto create(String nomeUtente, PutProfiloDto nuovoProfiloDto) throws InvalidParameterException {

        log.trace("Nome utente profilo da creare: {}", nomeUtente);

        // Verifichiamo l'integrità dei dati
        nuovoProfiloDto.setNomeUtente(nomeUtente);
        checkFieldsValid(nuovoProfiloDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        Profilo nuovoProfilo = putProfiloMapper.toEntity(nuovoProfiloDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovoProfilo: {}", nuovoProfilo);

        // Recuperiamo le associazioni
        convertRelations(nuovoProfiloDto, nuovoProfilo);

        log.debug("Verifico che l'email scelta non sia già utilizzata nell'account di altri profili...");

        checkEmailNotAlreadyTaken(nuovoProfiloDto.getEmail());

        log.debug("L'email non è utilizzata in account di altri profili.");

        log.trace("nuovoProfilo: {}", nuovoProfilo);

        log.debug("Salvo il profilo nel database...");

        // Registriamo l'entità
        Profilo savedProfilo = profiloRepository.save(nuovoProfilo);

        log.trace("savedProfilo: {}", savedProfilo);
        log.debug("Profilo salvato correttamente nel database con nome utente {}...", savedProfilo.getNomeUtente());

        return profiloMapper.toDto(savedProfilo);
    }

    private void checkEmailNotAlreadyTaken(String email) throws InvalidParameterException {

        // Recupero la lista di account che hanno la stessa email
        List<Account> foundAccounts = accountRepository.findByEmailIs(email, Pageable.unpaged()).toList();

        log.trace("foundAccounts: {}", foundAccounts);

        // Se l'email è gia stata utilizzata, allora mando l'eccezione
        if (!foundAccounts.isEmpty()) {
            log.warn("Impossibile associare l'email '{}' all'account di questo profilo poichè è già associata all'account di un altro profilo!", email);

            throw new InvalidParameterException("L'email '" + email + "' è già associata all'account di un altro profilo!");
        }
    }

    @Override
    public Page<ProfiloDto> findAll(Pageable pageable) {

        log.debug("Recupero i profili dal database...");

        // Recuperiamo tutte le entità
        Page<Profilo> foundProfili = profiloRepository.findAll(pageable);

        log.trace("foundProfili: {}", foundProfili);
        log.debug("Profili recuperati dal database.");

        return foundProfili.map(profiloMapper::toDto);
    }

    @Override
    public Optional<ProfiloDto> findOne(String nomeUtente) {

        log.trace("Nome utente profilo da recuperare: {}", nomeUtente);
        log.debug(LOG_RECUPERO_PROFILO);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Profilo> foundProfilo = profiloRepository.findById(nomeUtente);

        log.trace(LOG_FOUND_PROFILO, foundProfilo);
        log.debug(LOG_PROFILO_RECUPERATO);

        return foundProfilo.map(profiloMapper::toDto);
    }

    @Override
    public boolean isExists(String nomeUtente) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return profiloRepository.existsById(nomeUtente);
    }

    @Override
    @Transactional
    public ProfiloDto fullUpdate(String nomeUtente, PutProfiloDto updatedProfiloDto) throws InvalidParameterException {

        log.trace("Nome utente profilo da sostituire: {}", nomeUtente);

        this.delete(nomeUtente);

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(nomeUtente, updatedProfiloDto);
    }

    @Override
    public ProfiloDto partialUpdate(String nomeUtente, ProfiloDto updatedProfiloDto) throws InvalidParameterException {

        log.trace("Nome utente profilo da aggiornare: {}", nomeUtente);

        updatedProfiloDto.setNomeUtente(nomeUtente);

        log.debug(LOG_RECUPERO_PROFILO);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Profilo> foundProfilo = profiloRepository.findById(nomeUtente);

        log.trace(LOG_FOUND_PROFILO, foundProfilo);
        log.debug(LOG_PROFILO_RECUPERATO);

        if (foundProfilo.isEmpty()) {
            log.warn("Il nome utente '{}' non corrisponde a nessun profilo esistente!", nomeUtente);

            throw new UpdateRuntimeException("Il nome utente '" + nomeUtente + "' non corrisponde a nessun profilo esistente!");
        } else {

            // Recuperiamo l'entità dal wrapping Optional
            Profilo existingProfilo = foundProfilo.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedProfiloDto, existingProfilo);

            return profiloMapper.toDto(profiloRepository.save(existingProfilo));
        }
    }

    @Override
    public void delete(String nomeUtente) {

        log.trace("Nome utente profilo da eliminare: {}", nomeUtente);
        log.debug("Elimino il profilo dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        profiloRepository.deleteById(nomeUtente);

        log.debug("Profilo eliminato dal database.");
    }

    @Override
    public void checkFieldsValid(ProfiloDto profiloDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di profilo...");

        checkNomeUtenteValid(profiloDto.getNomeUtente());
        checkProfilePictureValid(profiloDto.getProfilePicture());
        checkAnagraficaValid(profiloDto.getAnagrafica());
        checkAccountsValid(profiloDto.getAccountsShallow());

        log.debug("Integrità dei dati di profilo verificata.");
    }

    private void checkNomeUtenteValid(String nomeUtente) throws InvalidParameterException {

        log.trace("Controllo che 'nomeUtente' sia valido...");

        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");
        else if (!nomeUtente.matches("^[A-Za-z0-9+_.-]+$")) // Il nome utente non può contenere spazi o caratteri speciali
            throw new InvalidParameterException("Formato nome utente non valido!");

        log.trace("'nomeUtente' valido.");
    }

    private void checkProfilePictureValid(byte[] profilePicture) throws InvalidParameterException {

        log.trace("Controllo che 'profilePicture' sia valido...");

        if (profilePicture == null)
            throw new InvalidParameterException("La profile picture non può essere null!");

        log.trace("'profilePicture' valido.");
    }

    private void checkAnagraficaValid(AnagraficaProfiloDto anagrafica) throws InvalidParameterException {

        log.trace("Controllo che 'anagrafica' sia valido...");

        if (anagrafica == null)
            throw new InvalidParameterException("L'anagrafica non può essere null!");

        checkNomeValid(anagrafica.getNome());
        checkCognomeValid(anagrafica.getCognome());
        checkDataNascitaValid(anagrafica.getDataNascita());

        log.trace("'anagrafica' valido.");
    }

    private void checkNomeValid(String nome) throws InvalidParameterException {

        log.trace("Controllo che 'nome' sia valido...");

        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");

        log.trace("'nome' valido.");
    }

    private void checkCognomeValid(String cognome) throws InvalidParameterException {

        log.trace("Controllo che 'cognome' sia valido...");

        if (cognome == null)
            throw new InvalidParameterException("Il cognome non può essere null!");
        else if (cognome.isBlank())
            throw new InvalidParameterException("Il cognome non può essere vuoto!");

        log.trace("'cognome' valido.");
    }

    private void checkDataNascitaValid(LocalDate dataNascita) throws InvalidParameterException {

        log.trace("Controllo che 'dataNascita' sia valido...");

        if (dataNascita == null)
            throw new InvalidParameterException("La data di nascita non può essere null!");
        else if (dataNascita.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di nascita non può essere successiva alla data odierna!");

        log.trace("'dataNascita' valido.");
    }


    private void checkAccountsValid(Set<AccountShallowDto> accounts) throws InvalidParameterException {

        log.trace("Controllo che 'accounts' sia valido...");

        if (accounts == null)
            throw new InvalidParameterException("La lista di accounts non può essere null!");
        else if (accounts.isEmpty())
            throw new InvalidParameterException("Ci deve essere almeno un account associato al profilo!");
        else if (accounts.size() > 2)
            throw new InvalidParameterException("Non possono esserci più di due account associati al profilo!");

        log.trace("'accounts' valido.");
    }

    @Override
    public void checkFieldsValid(PutProfiloDto putProfiloDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati della creazione profilo...");

        checkNomeUtenteValid(putProfiloDto.getNomeUtente());
        checkProfilePictureValid(putProfiloDto.getProfilePicture());
        checkAnagraficaValid(putProfiloDto.getAnagrafica());
        checkEmailValid(putProfiloDto.getEmail());
        checkPasswordValid(putProfiloDto.getPassword());

        log.debug("Integrità dei dati della creazione profilo verificata.");
    }

    private void checkEmailValid(String email) throws InvalidParameterException {

        log.trace("Controllo che 'email' sia valido...");

        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");

        log.trace("'email' valido.");
    }

    private void checkPasswordValid(String password) throws InvalidParameterException {

        log.trace("Controllo che 'password' sia valido...");

        if (password == null)
            throw new InvalidParameterException("La password non può essere null!");
        else if (password.isBlank())
            throw new InvalidParameterException("La password non può essere vuota!");

        log.trace("'password' valido.");
    }

    @Override
    public void convertRelations(PutProfiloDto putProfiloDto, Profilo profilo) {

        log.debug("Recupero le associazioni della creazione profilo...");

        // Non ci sono relazioni

        log.debug("Associazioni della creazione profilo recuperate.");
    }

    @Override
    public void convertRelations(ProfiloDto profiloDto, Profilo profilo) throws InvalidParameterException {

        log.debug("Recupero le associazioni del profilo...");

        convertAccounts(profiloDto.getAccountsShallow(), profilo);

        log.debug("Associazioni del profilo recuperate.");
    }

    private void convertAccounts(Set<AccountShallowDto> accountShallow, Profilo profilo) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'accounts'...");

        profilo.getAccounts().clear();

        if (accountShallow != null) {

            for (AccountShallowDto accountShallowDto : accountShallow) {

                Account convertedAccount = relationsConverter.convertAccountShallowRelation(accountShallowDto);

                if (convertedAccount != null) {
                    profilo.addAccount(convertedAccount);
                    convertedAccount.setProfilo(profilo);
                }
            }
        }

        log.trace("'accounts' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(ProfiloDto updatedProfiloDto, Profilo existingProfilo) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di profilo richieste...");

        ifPresentUpdateProfilePicture(updatedProfiloDto.getProfilePicture(), existingProfilo);
        ifPresentUpdateAnagrafica(updatedProfiloDto.getAnagrafica(), existingProfilo);
        ifPresentUpdateLinks(updatedProfiloDto.getLinks(), existingProfilo);

        log.debug("Modifiche di profilo effettuate correttamente.");

        // Non è possibile modificare l'associazione "accounts" tramite la risorsa "profili"
    }

    private void ifPresentUpdateProfilePicture(byte[] updatedProfilePicture, Profilo existingProfilo) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'profilePicture'...");

        if (updatedProfilePicture != null) {
            checkProfilePictureValid(updatedProfilePicture);
            existingProfilo.setProfilePicture(updatedProfilePicture);
        }

        log.trace("'profilePicture' modificato correttamente.");
    }

    private void ifPresentUpdateAnagrafica(AnagraficaProfiloDto updatedAnagraficaDto, Profilo existingProfilo) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'anagrafica'...");

        AnagraficaProfilo existingAnagrafica = existingProfilo.getAnagrafica();

        if (updatedAnagraficaDto != null) {
            if (existingAnagrafica == null) {
                checkAnagraficaValid(updatedAnagraficaDto);
                existingProfilo.setAnagrafica(anagraficaProfiloMapper.toEntity(updatedAnagraficaDto));
            } else {
                ifPresentUpdateNome(updatedAnagraficaDto.getNome(), existingAnagrafica);
                ifPresentUpdateCognome(updatedAnagraficaDto.getCognome(), existingAnagrafica);
                ifPresentUpdateDataNascita(updatedAnagraficaDto.getDataNascita(), existingAnagrafica);
                ifPresentUpdateAreaGeografica(updatedAnagraficaDto.getAreaGeografica(), existingAnagrafica);
                ifPresentUpdateGenere(updatedAnagraficaDto.getGenere(), existingAnagrafica);
                ifPresentUpdateBiografia(updatedAnagraficaDto.getBiografia(), existingAnagrafica);
            }
        }

        log.trace("'anagrafica' modificato correttamente.");
    }

    private void ifPresentUpdateNome(String updatedNome, AnagraficaProfilo existingAnagrafica) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'nome'...");

        if (updatedNome != null) {
            this.checkNomeValid(updatedNome);
            existingAnagrafica.setNome(updatedNome);
        }

        log.trace("'nome' modificato correttamente.");
    }

    private void ifPresentUpdateCognome(String updatedCognome, AnagraficaProfilo existingAnagrafica) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'cognome'...");

        if (updatedCognome != null) {
            this.checkCognomeValid(updatedCognome);
            existingAnagrafica.setCognome(updatedCognome);
        }

        log.trace("'cognome' modificato correttamente.");
    }

    private void ifPresentUpdateDataNascita(LocalDate updatedDataNascita, AnagraficaProfilo existingAnagrafica) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'dataNascita'...");

        if (updatedDataNascita != null) {
            this.checkDataNascitaValid(updatedDataNascita);
            existingAnagrafica.setDataNascita(updatedDataNascita);
        }

        log.trace("'dataNascita' modificato correttamente.");
    }

    private void ifPresentUpdateAreaGeografica(String updatedAreaGeografica, AnagraficaProfilo existingAnagrafica) {

        log.trace("Effettuo la modifica di 'areaGeografica'...");

        if (updatedAreaGeografica != null) {
            existingAnagrafica.setAreaGeografica(updatedAreaGeografica);
        }

        log.trace("'areaGeografica' modificato correttamente.");
    }

    private void ifPresentUpdateGenere(String updatedGenere, AnagraficaProfilo existingAnagrafica) {

        log.trace("Effettuo la modifica di 'genere'...");

        if (updatedGenere != null) {
            existingAnagrafica.setGenere(updatedGenere);
        }

        log.trace("'genere' modificato correttamente.");
    }

    private void ifPresentUpdateBiografia(String updatedBiografia, AnagraficaProfilo existingAnagrafica) {

        log.trace("Effettuo la modifica di 'biografia'...");

        if (updatedBiografia != null) {
            existingAnagrafica.setBiografia(updatedBiografia);
        }

        log.trace("'biografia' modificato correttamente.");
    }

    private void ifPresentUpdateLinks(LinksProfiloDto updatedLinksDto, Profilo existingProfilo) {

        log.trace("Effettuo la modifica di 'links'...");

        LinksProfilo existingLinks = existingProfilo.getLinks();

        if (updatedLinksDto != null) {
            if (existingLinks == null) {
                //Non ci sono vincoli sui links -> Non c'è un "isLinksValid"
                existingProfilo.setLinks(linksProfiloMapper.toEntity(updatedLinksDto));
            } else {
                ifPresentUpdateLinkPersonale(updatedLinksDto.getLinkPersonale(), existingLinks);
                ifPresentUpdateLinkInstagram(updatedLinksDto.getLinkInstagram(), existingLinks);
                ifPresentUpdateLinkFacebook(updatedLinksDto.getLinkFacebook(), existingLinks);
                ifPresentUpdateLinkGitHub(updatedLinksDto.getLinkGitHub(), existingLinks);
                ifPresentUpdateLinkX(updatedLinksDto.getLinkX(), existingLinks);

            }
        }

        log.trace("'links' modificato correttamente.");
    }

    private void ifPresentUpdateLinkPersonale(String updatedLinkPersonale, LinksProfilo existingLinks) {

        log.trace("Effettuo la modifica di 'linkPersonale'...");

        if (updatedLinkPersonale != null) {
            existingLinks.setLinkPersonale(updatedLinkPersonale);
        }

        log.trace("'linkPersonale' modificato correttamente.");
    }

    private void ifPresentUpdateLinkInstagram(String updatedLinkInstagram, LinksProfilo existingLinks) {

        log.trace("Effettuo la modifica di 'linkInstagram'...");

        if (updatedLinkInstagram != null) {
            existingLinks.setLinkInstagram(updatedLinkInstagram);
        }

        log.trace("'linkInstagram' modificato correttamente.");
    }

    private void ifPresentUpdateLinkFacebook(String updatedLinkFacebook, LinksProfilo existingLinks) {

        log.trace("Effettuo la modifica di 'linkFacebok'...");

        if (updatedLinkFacebook != null) {
            existingLinks.setLinkFacebook(updatedLinkFacebook);
        }

        log.trace("'linkFacebok' modificato correttamente.");
    }

    private void ifPresentUpdateLinkGitHub(String updatedLinkGitHub, LinksProfilo existingLinks) {

        log.trace("Effettuo la modifica di 'linkGitHub'...");

        if (updatedLinkGitHub != null) {
            existingLinks.setLinkGitHub(updatedLinkGitHub);
        }

        log.trace("'linkGitHub' modificato correttamente.");
    }

    private void ifPresentUpdateLinkX(String updatedLinkX, LinksProfilo existingLinks) {

        log.trace("Effettuo la modifica di 'linkX'...");

        if (updatedLinkX != null) {
            existingLinks.setLinkX(updatedLinkX);
        }

        log.trace("'linkX' modificato correttamente.");
    }
}