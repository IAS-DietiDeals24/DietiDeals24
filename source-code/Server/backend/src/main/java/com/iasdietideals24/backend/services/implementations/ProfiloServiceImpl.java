package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
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
import com.iasdietideals24.backend.repositories.CompratoreRepository;
import com.iasdietideals24.backend.repositories.ProfiloRepository;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
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
    private final CompratoreRepository compratoreRepository;
    private final VenditoreRepository venditoreRepository;

    public ProfiloServiceImpl(
            ProfiloMapper profiloMapper,
            AnagraficaProfiloMapper anagraficaProfiloMapper,
            LinksProfiloMapper linksProfiloMapper,
            ProfiloRepository profiloRepository,
            CompratoreRepository compratoreRepository,
            VenditoreRepository venditoreRepository) {
        this.profiloMapper = profiloMapper;
        this.anagraficaProfiloMapper = anagraficaProfiloMapper;
        this.linksProfiloMapper = linksProfiloMapper;
        this.profiloRepository = profiloRepository;
        this.compratoreRepository = compratoreRepository;
        this.venditoreRepository = venditoreRepository;

    }

    @Override
    public ProfiloDto create(String nomeUtente, PutProfiloDto nuovoProfiloDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        nuovoProfiloDto.setNomeUtente(nomeUtente);
        this.validateData(nuovoProfiloDto);

        // Convertiamo a entità e la registriamo
        Profilo nuovoProfilo = profiloMapper.toEntity(nuovoProfiloDto);
        Profilo savedProfilo = profiloRepository.save(nuovoProfilo);

        return profiloMapper.toDto(savedProfilo);
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
            Profilo existingProfilo = foundProfilo.get();
            if (updatedProfiloDto.getProfilePicture() != null) {
                isProfilePictureValid(updatedProfiloDto.getProfilePicture());
                existingProfilo.setProfilePicture(updatedProfiloDto.getProfilePicture());
            }

            existingProfilo.setAnagrafica(
                    partialUpdateAnagraficaProfilo(
                            existingProfilo.getAnagrafica(),
                            updatedProfiloDto.getAnagrafica()
                    )
            );

            existingProfilo.setLinks(
                    partialUpdateLinksProfilo(
                            existingProfilo.getLinks(),
                            updatedProfiloDto.getLinks()
                    )
            );

            partialUpdateAccountsProfilo(
                    existingProfilo,
                    updatedProfiloDto.getAccountsShallow()
            );

            return profiloMapper.toDto(profiloRepository.save(existingProfilo));
        }
    }

    private AnagraficaProfilo partialUpdateAnagraficaProfilo(AnagraficaProfilo existingAnagrafica, AnagraficaProfiloDto updatedAnagraficaDto) throws InvalidParameterException {
        AnagraficaProfilo newAnagrafica = null;

        if (updatedAnagraficaDto != null) {
            if (existingAnagrafica == null) {
                newAnagrafica = anagraficaProfiloMapper.toEntity(updatedAnagraficaDto);
            } else {
                if (updatedAnagraficaDto.getNome() != null) {
                    this.isNomeValid(updatedAnagraficaDto.getNome());
                    existingAnagrafica.setNome(updatedAnagraficaDto.getNome());
                }
                if (updatedAnagraficaDto.getCognome() != null) {
                    this.isCognomeValid(updatedAnagraficaDto.getCognome());
                    existingAnagrafica.setCognome(updatedAnagraficaDto.getCognome());
                }
                if (updatedAnagraficaDto.getDataNascita() != null) {
                    this.isDataNascitaValid(updatedAnagraficaDto.getDataNascita());
                    existingAnagrafica.setDataNascita(updatedAnagraficaDto.getDataNascita());
                }
                if (updatedAnagraficaDto.getAreaGeografica() != null) {
                    existingAnagrafica.setAreaGeografica(updatedAnagraficaDto.getAreaGeografica());
                }
                if (updatedAnagraficaDto.getGenere() != null) {
                    existingAnagrafica.setGenere(updatedAnagraficaDto.getGenere());
                }
                if (updatedAnagraficaDto.getBiografia() != null) {
                    existingAnagrafica.setBiografia(updatedAnagraficaDto.getBiografia());
                }

                newAnagrafica = existingAnagrafica;
            }
        }

        return newAnagrafica;
    }

    private LinksProfilo partialUpdateLinksProfilo(LinksProfilo existingLinks, LinksProfiloDto updatedLinksDto) {
        LinksProfilo newLinks = null;

        if (updatedLinksDto != null) {
            if (existingLinks == null) {
                newLinks = linksProfiloMapper.toEntity(updatedLinksDto);
            } else {
                if (updatedLinksDto.getLinkPersonale() != null) {
                    existingLinks.setLinkPersonale(updatedLinksDto.getLinkPersonale());
                }
                if (updatedLinksDto.getLinkInstagram() != null) {
                    existingLinks.setLinkInstagram(updatedLinksDto.getLinkInstagram());
                }
                if (updatedLinksDto.getLinkFacebook() != null) {
                    existingLinks.setLinkFacebook(updatedLinksDto.getLinkFacebook());
                }
                if (updatedLinksDto.getLinkGitHub() != null) {
                    existingLinks.setLinkGitHub(updatedLinksDto.getLinkGitHub());
                }
                if (updatedLinksDto.getLinkX() != null) {
                    existingLinks.setLinkX(updatedLinksDto.getLinkX());
                }

                newLinks = existingLinks;
            }
        }

        return newLinks;
    }

    private void partialUpdateAccountsProfilo(Profilo existingProfilo, Set<AccountShallowDto> updatedAccounts) throws InvalidParameterException {

        if (updatedAccounts != null) {
            for (AccountShallowDto updatedAccountDto : updatedAccounts) {
                if (updatedAccountDto.getTipoAccount() == null) {
                    throw new InvalidTypeException("Il tipo account non può essere null!");
                } else if (updatedAccountDto.getTipoAccount().equals(Compratore.class.getSimpleName())) {
                    Optional<Compratore> existingCompratore = compratoreRepository.findById(updatedAccountDto.getEmail());
                    if (existingCompratore.isPresent()) {
                        existingProfilo.removeAccount(existingProfilo.getCompratore());
                        existingProfilo.addAccount(existingCompratore.get());
                    }  else {
                        throw new InvalidParameterException("L'email non corrisponde a nessun account compratore esistente!");
                    }
                } else if (updatedAccountDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
                    Optional<Venditore> existingVenditore = venditoreRepository.findById(updatedAccountDto.getEmail());
                    if (existingVenditore.isPresent()) {
                        existingProfilo.removeAccount(existingProfilo.getVenditore());
                        existingProfilo.addAccount(existingVenditore.get());
                    } else {
                        throw new InvalidParameterException("L'email non corrisponde a nessun account venditore esistente!");
                    }
                } else {
                    throw new InvalidTypeException();
                }
            }
        }
    }

    @Override
    public void delete(String nomeUtente) {

        // Eliminiamo l'entità con l'id passato per parametro
        profiloRepository.deleteById(nomeUtente);
    }

    @Override
    public void validateData(ProfiloDto nuovoProfiloDto) throws InvalidParameterException {
        isNomeUtenteValid(nuovoProfiloDto.getNomeUtente());
        isProfilePictureValid(nuovoProfiloDto.getProfilePicture());
        isAnagraficaValid(nuovoProfiloDto.getAnagrafica());
        isAccountsValid(nuovoProfiloDto.getAccountsShallow());
    }

    private void isNomeUtenteValid(String nomeUtente) throws InvalidParameterException {
        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");
    }

    private void isProfilePictureValid(byte[] profilePicture) throws InvalidParameterException {
        if (profilePicture == null)
            throw new InvalidParameterException("La profile picture non può essere null!");
    }

    private void isAnagraficaValid(AnagraficaProfiloDto anagrafica) throws InvalidParameterException {
        if (anagrafica == null)
            throw new InvalidParameterException("L'anagrafica non può essere null!");

        isNomeValid(anagrafica.getNome());
        isCognomeValid(anagrafica.getCognome());
        isDataNascitaValid(anagrafica.getDataNascita());
    }

    private void isNomeValid(String nome) throws InvalidParameterException {
        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");
    }

    private void isCognomeValid(String cognome) throws InvalidParameterException {
        if (cognome == null)
            throw new InvalidParameterException("Il cognome non può essere null!");
        else if (cognome.isBlank())
            throw new InvalidParameterException("Il cognome non può essere vuoto!");
    }

    private void isDataNascitaValid(LocalDate dataNascita) throws InvalidParameterException {
        if (dataNascita == null)
            throw new InvalidParameterException("La data di nascita non può essere null!");
        else if (dataNascita.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di nascita non può essere successiva alla data odierna!");
    }


    private void isAccountsValid(Set<AccountShallowDto> accounts) throws InvalidParameterException {
        if (accounts == null)
            throw new InvalidParameterException("La lista di accounts non può essere null!");
        else if (accounts.isEmpty())
            throw new InvalidParameterException("Ci deve essere almeno un account associato al profilo!");
        else if (accounts.size() > 2)
            throw new InvalidParameterException("Non possono esserci più di due account associati al profilo!");
    }

    @Override
    public void validateData(PutProfiloDto nuovoProfiloDto) throws InvalidParameterException {
        isNomeUtenteValid(nuovoProfiloDto.getNomeUtente());
        isProfilePictureValid(nuovoProfiloDto.getProfilePicture());
        isAnagraficaValid(nuovoProfiloDto.getAnagrafica());
        isEmailValid(nuovoProfiloDto.getEmail());
        isPasswordValid(nuovoProfiloDto.getPassword());
    }

    private void isEmailValid(String email) throws InvalidParameterException {
        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");
    }

    private void isPasswordValid(String password) throws InvalidParameterException {
        if (password == null)
            throw new InvalidParameterException("La password non può essere null!");
        else if (password.isBlank())
            throw new InvalidParameterException("La password non può essere vuota!");
    }
}