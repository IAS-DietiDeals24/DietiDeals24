package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.AnagraficaProfiloDto;
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
    private final ProfiloRepository profiloRepository;

    public ProfiloServiceImpl(ProfiloMapper profiloMapper, ProfiloRepository profiloRepository) {
        this.profiloMapper = profiloMapper;
        this.profiloRepository = profiloRepository;
    }

    @Override
    public ProfiloDto create(String nomeUtente, PutProfiloDto nuovoProfiloDto) throws InvalidParameterException {
        nuovoProfiloDto.setNomeUtente(nomeUtente);
        this.validateData(nuovoProfiloDto);

        Profilo nuovoProfilo = profiloMapper.toEntity(nuovoProfiloDto);
        Profilo savedProfilo = profiloRepository.save(nuovoProfilo);

        return profiloMapper.toDto(savedProfilo);
    }

    @Override
    public Page<ProfiloDto> findAll(Pageable pageable) {
        return profiloRepository.findAll(pageable).map(profiloMapper::toDto);
    }

    @Override
    public Optional<ProfiloDto> findOne(String nomeUtente) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(String nomeUtente) {
        return false;
    }

    @Override
    public ProfiloDto fullUpdate(String nomeUtente, PutProfiloDto nuovoProfiloDto) throws InvalidParameterException {
        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(nomeUtente, nuovoProfiloDto);
    }

    @Override
    public ProfiloDto partialUpdate(String nomeUtente, ProfiloDto nuovoProfiloDto) {
        return null;
    }

    @Override
    public void delete(String nomeUtente) {
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
            throw new InvalidParameterException("La data di nascita non può essere antecedente alla data odierna!");
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