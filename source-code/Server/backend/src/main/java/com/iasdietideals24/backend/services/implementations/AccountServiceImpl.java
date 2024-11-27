package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import com.iasdietideals24.backend.mapstruct.mappers.TokensAccountMapper;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private final TokensAccountMapper tokensAccountMapper;
    private final RelationsConverter relationsConverter;

    protected AccountServiceImpl(TokensAccountMapper tokensAccountMapper, RelationsConverter relationsConverter) {
        this.tokensAccountMapper = tokensAccountMapper;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AccountDto accountDto) throws InvalidParameterException {
        checkEmailValid(accountDto.getEmail());
        checkPasswordValid(accountDto.getPassword());
        checkProfiloShallowValid(accountDto.getProfiloShallow());
    }

    private void checkEmailValid(String email) throws InvalidParameterException {
        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");
    }

    private void checkPasswordValid(String password) throws InvalidParameterException {
        if (password == null)
            throw new InvalidParameterException("La password non può essere null!");
        else if (password.isBlank())
            throw new InvalidParameterException("La password non può essere vuota!");
    }

    private void checkProfiloShallowValid(ProfiloShallowDto profiloShallow) throws InvalidParameterException {
        if (profiloShallow == null)
            throw new InvalidParameterException();

        checkNomeUtenteValid(profiloShallow.getNomeUtente());
    }

    private void checkNomeUtenteValid(String nomeUtente) throws InvalidParameterException {
        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");
    }

    @Override
    public void convertRelations(AccountDto accountDto, Account account) throws InvalidParameterException {
        convertProfiloShallow(accountDto.getProfiloShallow(), account);
        convertNotificheInviateShallow(accountDto.getNotificheInviateShallow(), account);
        convertNotificheRicevuteShallow(accountDto.getNotificheRicevuteShallow(), account);
    }

    private void convertProfiloShallow(ProfiloShallowDto profiloShallowDto, Account nuovoAccount) throws InvalidParameterException {

        Profilo convertedProfilo = relationsConverter.convertProfiloShallowRelation(profiloShallowDto);

        if (convertedProfilo != null) {
            if (nuovoAccount instanceof Compratore && convertedProfilo.getCompratore() != null)
                throw new InvalidParameterException("Il profilo \"" + convertedProfilo.getNomeUtente() + "\" è già associato a un account compratore! Eliminare prima quello precedente.");
            else if (nuovoAccount instanceof Venditore && convertedProfilo.getVenditore() != null)
                throw new InvalidParameterException("Il profilo \"" + convertedProfilo.getNomeUtente() + "\" è già associato a un account venditore! Eliminare prima quello precedente.");
            else {
                nuovoAccount.setProfilo(convertedProfilo);
                convertedProfilo.addAccount(nuovoAccount);
            }
        }
    }

    private void convertNotificheInviateShallow(Set<NotificaShallowDto> notificheInviateShallowDto, Account nuovoAccount) throws InvalidParameterException {
        if (notificheInviateShallowDto != null) {
            for (NotificaShallowDto notificaShallowDto : notificheInviateShallowDto) {

                Notifica convertedNotifica = relationsConverter.convertNotificaShallowRelation(notificaShallowDto);

                if (convertedNotifica != null) {
                    nuovoAccount.addNotificaInviata(convertedNotifica);
                    convertedNotifica.setMittente(nuovoAccount);
                }
            }
        }
    }

    private void convertNotificheRicevuteShallow(Set<NotificaShallowDto> notificheRicevuteShallowDto, Account nuovoAccount) throws InvalidParameterException {
        if (notificheRicevuteShallowDto != null) {
            for (NotificaShallowDto notificaShallowDto : notificheRicevuteShallowDto) {

                Notifica convertedNotifica = relationsConverter.convertNotificaShallowRelation(notificaShallowDto);

                if (convertedNotifica != null) {
                    nuovoAccount.addNotificaInviata(convertedNotifica);
                    convertedNotifica.setMittente(nuovoAccount);
                }
            }
        }
    }

    @Override
    public void updatePresentFields(AccountDto updatedAccountDto, Account existingAccount) throws InvalidParameterException {
        ifPresentUpdateEmail(updatedAccountDto.getEmail(), existingAccount);
        ifPresentUpdatePassword(updatedAccountDto.getPassword(), existingAccount);
        ifPresentUpdateTokens(updatedAccountDto.getTokens(), existingAccount);

        // Non è possibile modificare le associazioni "profilo", "notificheInviate", "notificheRicevute" tramite la risorsa "accounts"
    }

    private void ifPresentUpdateEmail(String updatedEmail, Account existingAccount) throws InvalidParameterException {
        if (updatedEmail != null) {
            checkEmailValid(updatedEmail);
            existingAccount.setEmail(updatedEmail);
        }
    }

    private void ifPresentUpdatePassword(String updatedPassword, Account existingAccount) throws InvalidParameterException {
        if (updatedPassword != null) {
            checkPasswordValid(updatedPassword);
            existingAccount.setPassword(updatedPassword);
        }
    }

    private void ifPresentUpdateTokens(TokensAccountDto updatedTokensDto, Account existingAccount) {
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

    private void ifPresentUpdateIdFacebook(String updatedIdFacebook, TokensAccount existingTokens) {
        if (updatedIdFacebook != null) {
            existingTokens.setIdFacebook(updatedIdFacebook);
        }
    }

    private void ifPresentUpdateIdGoogle(String updatedIdGoogle, TokensAccount existingTokens) {
        if (updatedIdGoogle != null) {
            existingTokens.setIdGoogle(updatedIdGoogle);
        }
    }

    private void ifPresentUpdateIdX(String updatedIdX, TokensAccount existingTokens) {
        if (updatedIdX != null) {
            existingTokens.setIdX(updatedIdX);
        }
    }

    private void ifPresentUpdateIdGitHub(String updatedIdGitHub, TokensAccount existingTokens) {
        if (updatedIdGitHub != null) {
            existingTokens.setIdGitHub(updatedIdGitHub);
        }
    }

    @Override
    public boolean isLastAccountOfProfilo(Account account) throws InvalidParameterException {
        Profilo profiloAssociato = account.getProfilo();
        if (profiloAssociato == null)
            throw new InvalidParameterException("Il profilo associato all'account \"" + account.getIdAccount() + "\" è null!");

        return (profiloAssociato.getAccounts().size() == 1);
    }
}
