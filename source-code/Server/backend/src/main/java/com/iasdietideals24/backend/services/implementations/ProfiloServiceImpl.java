package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.AnagraficaProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.LinksProfiloDto;
import com.iasdietideals24.backend.mapstruct.mappers.AnagraficaProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.LinksProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.ProfiloMapper;
import com.iasdietideals24.backend.repositories.ProfiloRepository;
import com.iasdietideals24.backend.services.ProfiloService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class ProfiloServiceImpl implements ProfiloService {

    private final ProfiloMapper profiloMapper;
    private final AnagraficaProfiloMapper anagraficaProfiloMapper;
    private final LinksProfiloMapper linksProfiloMapper;

    private final ProfiloRepository profiloRepository;

    public ProfiloServiceImpl(ProfiloMapper profiloMapper,
                              AnagraficaProfiloMapper anagraficaProfiloMapper,
                              LinksProfiloMapper linksProfiloMapper,
                              ProfiloRepository profiloRepository) {

        this.profiloMapper = profiloMapper;
        this.anagraficaProfiloMapper = anagraficaProfiloMapper;

        this.linksProfiloMapper = linksProfiloMapper;
        this.profiloRepository = profiloRepository;
    }

    @Override
    public ProfiloDto create(String nomeUtente, PutProfiloDto nuovoProfiloDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        nuovoProfiloDto.setNomeUtente(nomeUtente);
        checkFieldsValid(nuovoProfiloDto);

        // Convertiamo a entità
        Profilo nuovoProfilo = profiloMapper.toEntity(nuovoProfiloDto);

        // Recuperiamo le associazioni
        convertRelations(nuovoProfiloDto, nuovoProfilo);

        // Registriamo l'entità
        Profilo savedProfilo = profiloRepository.save(nuovoProfilo);

        return profiloMapper.toDto(savedProfilo);
    }

    public void convertRelations(PutProfiloDto putProfiloDto, Profilo profilo) {
        // Non ci sono relazioni
    }

    @Override
    public Page<ProfiloDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<Profilo> foundProfili = profiloRepository.findAll(pageable);

        return foundProfili.map(profiloMapper::toDto);
    }

    @Override
    public Optional<ProfiloDto> findOne(String nomeUtente) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Profilo> foundProfilo = profiloRepository.findById(nomeUtente);

        return foundProfilo.map(profiloMapper::toDto);
    }

    @Override
    public boolean isExists(String nomeUtente) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return profiloRepository.existsById(nomeUtente);
    }

    @Override
    public ProfiloDto fullUpdate(String nomeUtente, PutProfiloDto updatedProfiloDto) throws InvalidParameterException {

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(nomeUtente, updatedProfiloDto);
    }

    @Override
    public ProfiloDto partialUpdate(String nomeUtente, ProfiloDto updatedProfiloDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedProfiloDto.setNomeUtente(nomeUtente);
        Optional<Profilo> foundProfilo = profiloRepository.findById(nomeUtente);

        if (foundProfilo.isEmpty())
            throw new UpdateRuntimeException("Il nome utente non corrisponde a nessun profilo esistente!");
        else {
            // Recuperiamo l'entità Profilo dal wrapping Optional
            Profilo existingProfilo = foundProfilo.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedProfiloDto, existingProfilo);

            //Non è possibile modificare l'associazione "accounts" tramite la risorsa "profili"

            return profiloMapper.toDto(profiloRepository.save(existingProfilo));
        }
    }

    public void updatePresentFields(ProfiloDto updatedProfiloDto, Profilo existingProfilo) throws InvalidParameterException {
        ifPresentUpdateProfilePicture(updatedProfiloDto.getProfilePicture(), existingProfilo);
        ifPresentUpdateAnagrafica(updatedProfiloDto.getAnagrafica(), existingProfilo);
        ifPresentUpdateLinks(updatedProfiloDto.getLinks(), existingProfilo);
    }

    void ifPresentUpdateProfilePicture(byte[] updatedProfilePicture, Profilo existingProfilo) throws InvalidParameterException {
        if (updatedProfilePicture != null) {
            checkProfilePictureValid(updatedProfilePicture);
            existingProfilo.setProfilePicture(updatedProfilePicture);
        }
    }

    void ifPresentUpdateAnagrafica(AnagraficaProfiloDto updatedAnagraficaDto, Profilo existingProfilo) throws InvalidParameterException {
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
    }

    void ifPresentUpdateNome(String updatedNome, AnagraficaProfilo existingAnagrafica) throws InvalidParameterException {
        if (updatedNome != null) {
            this.checkNomeValid(updatedNome);
            existingAnagrafica.setNome(updatedNome);
        }
    }

    void ifPresentUpdateCognome(String updatedCognome, AnagraficaProfilo existingAnagrafica) throws InvalidParameterException {
        if (updatedCognome != null) {
            this.checkCognomeValid(updatedCognome);
            existingAnagrafica.setCognome(updatedCognome);
        }
    }

    void ifPresentUpdateDataNascita(LocalDate updatedDataNascita, AnagraficaProfilo existingAnagrafica) throws InvalidParameterException {
        if (updatedDataNascita != null) {
            this.checkDataNascitaValid(updatedDataNascita);
            existingAnagrafica.setDataNascita(updatedDataNascita);
        }
    }

    void ifPresentUpdateAreaGeografica(String updatedAreaGeografica, AnagraficaProfilo existingAnagrafica) {
        if (updatedAreaGeografica != null) {
            existingAnagrafica.setAreaGeografica(updatedAreaGeografica);
        }
    }

    void ifPresentUpdateGenere(String updatedGenere, AnagraficaProfilo existingAnagrafica) {
        if (updatedGenere != null) {
            existingAnagrafica.setGenere(updatedGenere);
        }
    }

    void ifPresentUpdateBiografia(String updatedBiografia, AnagraficaProfilo existingAnagrafica) {
        if (updatedBiografia != null) {
            existingAnagrafica.setBiografia(updatedBiografia);
        }
    }

    void ifPresentUpdateLinks(LinksProfiloDto updatedLinksDto, Profilo existingProfilo) {
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
    }

    void ifPresentUpdateLinkPersonale(String updatedLinkPersonale, LinksProfilo existingLinks) {
        if (updatedLinkPersonale != null) {
            existingLinks.setLinkPersonale(updatedLinkPersonale);
        }
    }

    void ifPresentUpdateLinkInstagram(String updatedLinkInstagram, LinksProfilo existingLinks) {
        if (updatedLinkInstagram != null) {
            existingLinks.setLinkInstagram(updatedLinkInstagram);
        }
    }

    void ifPresentUpdateLinkFacebook(String updatedLinkFacebook, LinksProfilo existingLinks) {
        if (updatedLinkFacebook != null) {
            existingLinks.setLinkFacebook(updatedLinkFacebook);
        }
    }

    void ifPresentUpdateLinkGitHub(String updatedLinkGitHub, LinksProfilo existingLinks) {
        if (updatedLinkGitHub != null) {
            existingLinks.setLinkGitHub(updatedLinkGitHub);
        }
    }

    void ifPresentUpdateLinkX(String updatedLinkX, LinksProfilo existingLinks) {
        if (updatedLinkX != null) {
            existingLinks.setLinkX(updatedLinkX);
        }
    }

    @Override
    public void delete(String nomeUtente) {

        // Eliminiamo l'entità con l'id passato per parametro
        profiloRepository.deleteById(nomeUtente);
    }

    @Override
    public void checkFieldsValid(ProfiloDto nuovoProfiloDto) throws InvalidParameterException {
        checkNomeUtenteValid(nuovoProfiloDto.getNomeUtente());
        checkProfilePictureValid(nuovoProfiloDto.getProfilePicture());
        checkAnagraficaValid(nuovoProfiloDto.getAnagrafica());
        checkAccountsValid(nuovoProfiloDto.getAccountsShallow());
    }

    void checkNomeUtenteValid(String nomeUtente) throws InvalidParameterException {
        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");
    }

    void checkProfilePictureValid(byte[] profilePicture) throws InvalidParameterException {
        if (profilePicture == null)
            throw new InvalidParameterException("La profile picture non può essere null!");
    }

    void checkAnagraficaValid(AnagraficaProfiloDto anagrafica) throws InvalidParameterException {
        boolean valid = false;

        if (anagrafica == null)
            throw new InvalidParameterException("L'anagrafica non può essere null!");

        checkNomeValid(anagrafica.getNome());
        checkCognomeValid(anagrafica.getCognome());
        checkDataNascitaValid(anagrafica.getDataNascita());
    }

    void checkNomeValid(String nome) throws InvalidParameterException {
        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");
    }

    void checkCognomeValid(String cognome) throws InvalidParameterException {
        if (cognome == null)
            throw new InvalidParameterException("Il cognome non può essere null!");
        else if (cognome.isBlank())
            throw new InvalidParameterException("Il cognome non può essere vuoto!");
    }

    void checkDataNascitaValid(LocalDate dataNascita) throws InvalidParameterException {
        if (dataNascita == null)
            throw new InvalidParameterException("La data di nascita non può essere null!");
        else if (dataNascita.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di nascita non può essere successiva alla data odierna!");
    }


    void checkAccountsValid(Set<AccountShallowDto> accounts) throws InvalidParameterException {
        if (accounts == null)
            throw new InvalidParameterException("La lista di accounts non può essere null!");
        else if (accounts.isEmpty())
            throw new InvalidParameterException("Ci deve essere almeno un account associato al profilo!");
        else if (accounts.size() > 2)
            throw new InvalidParameterException("Non possono esserci più di due account associati al profilo!");
    }

    @Override
    public void checkFieldsValid(PutProfiloDto putProfiloDto) throws InvalidParameterException {
        checkNomeUtenteValid(putProfiloDto.getNomeUtente());
        checkProfilePictureValid(putProfiloDto.getProfilePicture());
        checkAnagraficaValid(putProfiloDto.getAnagrafica());
        checkEmailValid(putProfiloDto.getEmail());
        checkPasswordValid(putProfiloDto.getPassword());
    }

    void checkEmailValid(String email) throws InvalidParameterException {
        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");
    }

    void checkPasswordValid(String password) throws InvalidParameterException {
        if (password == null)
            throw new InvalidParameterException("La password non può essere null!");
        else if (password.isBlank())
            throw new InvalidParameterException("La password non può essere vuota!");
    }
}