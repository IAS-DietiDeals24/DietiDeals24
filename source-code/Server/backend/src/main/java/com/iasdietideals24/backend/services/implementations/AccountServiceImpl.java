package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import com.iasdietideals24.backend.mapstruct.mappers.NotificaMapper;
import com.iasdietideals24.backend.mapstruct.mappers.ProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.TokensAccountMapper;
import com.iasdietideals24.backend.repositories.NotificaRepository;
import com.iasdietideals24.backend.repositories.ProfiloRepository;
import com.iasdietideals24.backend.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private final ProfiloMapper profiloMapper;
    private final NotificaMapper notificaMapper;
    private final TokensAccountMapper tokensAccountMapper;

    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;

    protected AccountServiceImpl(ProfiloMapper profiloMapper, NotificaMapper notificaMapper, TokensAccountMapper tokensAccountMapper, ProfiloRepository profiloRepository, NotificaRepository notificaRepository) {
        this.profiloMapper = profiloMapper;
        this.notificaMapper = notificaMapper;
        this.tokensAccountMapper = tokensAccountMapper;

        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
    }

    @Override
    public void checkFieldsValid(AccountDto accountDto) throws InvalidParameterException {
        checkEmailValid(accountDto.getEmail());
        checkPasswordValid(accountDto.getPassword());
        checkProfiloShallowValid(accountDto.getProfiloShallow());
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

    void checkProfiloShallowValid(ProfiloShallowDto profiloShallow) throws InvalidParameterException {
        if (profiloShallow == null)
            throw new InvalidParameterException();

        checkNomeUtenteValid(profiloShallow.getNomeUtente());
    }

    void checkNomeUtenteValid(String nomeUtente) throws InvalidParameterException {
        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");
    }

    public void convertRelations(AccountDto accountDto, Account account) throws InvalidParameterException {
        convertProfiloShallow(accountDto.getProfiloShallow(), account);
        convertNotificheInviateShallow(accountDto.getNotificheInviateShallow(), account);
        convertNotificheRicevuteShallow(accountDto.getNotificheRicevuteShallow(), account);
    }

    void convertProfiloShallow(ProfiloShallowDto profiloShallowDto, Account nuovoAccount) throws InvalidParameterException {
        if (profiloShallowDto != null) {
            Profilo profilo = profiloMapper.toEntity(profiloShallowDto);
            Optional<Profilo> foundProfilo = profiloRepository.findById(profiloShallowDto.getNomeUtente());
            if (foundProfilo.isEmpty())
                throw new IdNotFoundException("Il nome utente \"" + profilo.getNomeUtente() + "\" non corrisponde a nessun profilo esistente!");
            else {
                if (nuovoAccount instanceof Compratore && foundProfilo.get().getCompratore() != null)
                    throw new InvalidParameterException("Il profilo \"" + "\" è già associato a un account compratore! Eliminare prima quello precedente.");
                else if (nuovoAccount instanceof Venditore && foundProfilo.get().getVenditore() != null)
                    throw new InvalidParameterException("Il profilo \"" + "\" è già associato a un account venditore! Eliminare prima quello precedente.");
                else {
                    nuovoAccount.setProfilo(foundProfilo.get());
                    foundProfilo.get().addAccount(nuovoAccount);
                }
            }
        }
    }

    void convertNotificheInviateShallow(Set<NotificaShallowDto> notificheInviateShallowDto, Account nuovoAccount) throws InvalidParameterException {
        if (notificheInviateShallowDto != null) {
            for (Notifica notifica : notificaMapper.toEntity(notificheInviateShallowDto)) {
                Optional<Notifica> foundNotifica = notificaRepository.findById(notifica.getIdNotifica());
                if (foundNotifica.isEmpty())
                    throw new IdNotFoundException("L'id notifica inviata \"" + notifica.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
                else {
                    nuovoAccount.addNotificaInviata(foundNotifica.get());
                    foundNotifica.get().setMittente(nuovoAccount);
                }
            }
        }
    }

    void convertNotificheRicevuteShallow(Set<NotificaShallowDto> notificheRicevuteShallowDto, Account nuovoAccount) throws InvalidParameterException {
        if (notificheRicevuteShallowDto != null) {
            for (Notifica notifica : notificaMapper.toEntity(notificheRicevuteShallowDto)) {
                Optional<Notifica> foundNotifica = notificaRepository.findById(notifica.getIdNotifica());
                if (foundNotifica.isEmpty())
                    throw new IdNotFoundException("L'id notifica ricevuta \"" + notifica.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
                else {
                    nuovoAccount.addNotificaRicevuta(foundNotifica.get());
                    foundNotifica.get().addDestinatario(nuovoAccount);
                }
            }
        }
    }

    public void updatePresentFields(AccountDto updatedAccountDto, Account existingAccount) throws InvalidParameterException {
        ifPresentUpdateEmail(updatedAccountDto.getEmail(), existingAccount);
        ifPresentUpdatePassword(updatedAccountDto.getPassword(), existingAccount);
        ifPresentUpdateTokens(updatedAccountDto.getTokens(), existingAccount);
    }

    void ifPresentUpdateEmail(String updatedEmail, Account existingAccount) throws InvalidParameterException {
        if (updatedEmail != null) {
            checkEmailValid(updatedEmail);
            existingAccount.setEmail(updatedEmail);
        }
    }

    void ifPresentUpdatePassword(String updatedPassword, Account existingAccount) throws InvalidParameterException {
        if (updatedPassword != null) {
            checkPasswordValid(updatedPassword);
            existingAccount.setPassword(updatedPassword);
        }
    }

    void ifPresentUpdateTokens(TokensAccountDto updatedTokensDto, Account existingAccount) {
        TokensAccount existingTokens = existingAccount.getTokens();

        if (updatedTokensDto != null) {
            if (existingTokens == null) {
                existingAccount.setTokens(tokensAccountMapper.toEntity(updatedTokensDto));
            } else {
                ifPresentUpdateIdFacebook(updatedTokensDto.getIdFacebook(), existingTokens);
                ifPresentUpdateIdGoogle(updatedTokensDto.getIdGoogle(), existingTokens);
                ifPresentUpdateIdX(updatedTokensDto.getIdX(), existingTokens);
                ifPresentUpdateIdGitHub(updatedTokensDto.getIdGitHub(), existingTokens);
            }
        }
    }

    void ifPresentUpdateIdFacebook(String updatedIdFacebook, TokensAccount existingTokens) {
        if (updatedIdFacebook != null) {
            existingTokens.setIdFacebook(updatedIdFacebook);
        }
    }

    void ifPresentUpdateIdGoogle(String updatedIdGoogle, TokensAccount existingTokens) {
        if (updatedIdGoogle != null) {
            existingTokens.setIdGoogle(updatedIdGoogle);
        }
    }

    void ifPresentUpdateIdX(String updatedIdX, TokensAccount existingTokens) {
        if (updatedIdX != null) {
            existingTokens.setIdX(updatedIdX);
        }
    }

    void ifPresentUpdateIdGitHub(String updatedIdGitHub, TokensAccount existingTokens) {
        if (updatedIdGitHub != null) {
            existingTokens.setIdGitHub(updatedIdGitHub);
        }
    }

    @Override
    public boolean isLastProfiloAccount(Account account) throws InvalidParameterException {
        Profilo profiloAssociato = account.getProfilo();
        if (profiloAssociato == null)
            throw new InvalidParameterException("Il profilo associato all'account \"" + account.getEmail() + "\" è null!");

        return (profiloAssociato.getAccounts().size() == 1);
    }
}
