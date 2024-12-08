package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import com.iasdietideals24.backend.mapstruct.mappers.TokensAccountMapper;
import com.iasdietideals24.backend.repositories.AccountRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final TokensAccountMapper tokensAccountMapper;
    private final AccountRepository accountRepository;

    private final RelationsConverter relationsConverter;

    protected AccountServiceImpl(TokensAccountMapper tokensAccountMapper,
                                 RelationsConverter relationsConverter,
                                 AccountRepository accountRepository) {
        this.tokensAccountMapper = tokensAccountMapper;
        this.relationsConverter = relationsConverter;
        this.accountRepository = accountRepository;
    }

    @Override
    public void checkFieldsValid(AccountDto accountDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di account...");

        checkEmailValid(accountDto.getEmail());
        checkPasswordValid(accountDto.getPassword());
        checkProfiloShallowValid(accountDto.getProfiloShallow());

        log.debug("Integrità dei dati di account verificata.");
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

    private void checkProfiloShallowValid(ProfiloShallowDto profiloShallow) throws InvalidParameterException {

        log.trace("Controllo che 'profiloShallow' sia valido...");

        if (profiloShallow == null)
            throw new InvalidParameterException();

        checkNomeUtenteValid(profiloShallow.getNomeUtente());

        log.trace("'profiloShallow' valido.");
    }

    private void checkNomeUtenteValid(String nomeUtente) throws InvalidParameterException {

        log.trace("Controllo che 'nomeUtente' sia valido...");

        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");

        log.trace("'nomeUtente' valido.");
    }

    @Override
    public void convertRelations(AccountDto accountDto, Account account) throws InvalidParameterException {

        log.debug("Recupero le associazioni di account...");

        convertProfiloShallow(accountDto.getProfiloShallow(), account);
        convertNotificheInviateShallow(accountDto.getNotificheInviateShallow(), account);
        convertNotificheRicevuteShallow(accountDto.getNotificheRicevuteShallow(), account);

        log.debug("Associazioni di account recuperate.");
    }

    private void convertProfiloShallow(ProfiloShallowDto profiloShallowDto, Account nuovoAccount) throws InvalidParameterException {

        log.trace("Converto l'associazione 'profilo'...");

        Profilo convertedProfilo = relationsConverter.convertProfiloShallowRelation(profiloShallowDto);

        if (convertedProfilo != null) {

            verifyAccountsProfiloCohesion(nuovoAccount, convertedProfilo);

            nuovoAccount.setProfilo(convertedProfilo);
            convertedProfilo.addAccount(nuovoAccount);
        }

        log.trace("'profilo' convertita correttamente.");
    }

    private void convertNotificheInviateShallow(Set<NotificaShallowDto> notificheInviateShallowDto, Account nuovoAccount) throws InvalidParameterException {

        log.trace("Converto l'associazione 'notificheInviate'...");

        nuovoAccount.getNotificheInviate().clear();

        if (notificheInviateShallowDto != null) {
            for (NotificaShallowDto notificaShallowDto : notificheInviateShallowDto) {

                Notifica convertedNotifica = relationsConverter.convertNotificaShallowRelation(notificaShallowDto);

                if (convertedNotifica != null) {
                    nuovoAccount.addNotificaInviata(convertedNotifica);
                    convertedNotifica.setMittente(nuovoAccount);
                }
            }
        }

        log.trace("'notificheInviate' convertita correttamente.");
    }

    private void convertNotificheRicevuteShallow(Set<NotificaShallowDto> notificheRicevuteShallowDto, Account nuovoAccount) throws InvalidParameterException {

        log.trace("Converto l'associazione 'notificheRicevute'...");

        nuovoAccount.getNotificheRicevute().clear();

        if (notificheRicevuteShallowDto != null) {
            for (NotificaShallowDto notificaShallowDto : notificheRicevuteShallowDto) {

                Notifica convertedNotifica = relationsConverter.convertNotificaShallowRelation(notificaShallowDto);

                if (convertedNotifica != null) {
                    nuovoAccount.addNotificaInviata(convertedNotifica);
                    convertedNotifica.setMittente(nuovoAccount);
                }
            }
        }

        log.trace("'notificheRicevute' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AccountDto updatedAccountDto, Account existingAccount) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di account richieste...");

        ifPresentUpdateEmail(updatedAccountDto.getEmail(), existingAccount);
        ifPresentUpdatePassword(updatedAccountDto.getPassword(), existingAccount);
        ifPresentUpdateTokens(updatedAccountDto.getTokens(), existingAccount);

        log.debug("Modifiche di account effettuate correttamente.");

        // Non è possibile modificare le associazioni "profilo", "notificheInviate", "notificheRicevute" tramite la risorsa "accounts"
    }

    private void ifPresentUpdateEmail(String updatedEmail, Account existingAccount) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'email'...");

        if (updatedEmail != null) {
            checkEmailValid(updatedEmail);
            existingAccount.setEmail(updatedEmail);

            // Controllo che l'email sia coerente rispetto a quella degli altri account
            Profilo existingProfilo = existingAccount.getProfilo();
            for (Account accountAssociato : existingProfilo.getAccounts()) {
                checkNuovoAccountCohesionWithOtherAccounts(accountAssociato, existingAccount);
            }

            // Controllo che l'email non sia già stata presa
            checkEmailNotAlreadyTaken(existingAccount);
        }

        log.trace("'email' modificato correttamente.");
    }

    private void ifPresentUpdatePassword(String updatedPassword, Account existingAccount) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'password'...");

        if (updatedPassword != null) {
            checkPasswordValid(updatedPassword);
            existingAccount.setPassword(updatedPassword);
        }

        log.trace("'password' modificato correttamente.");
    }

    private void ifPresentUpdateTokens(TokensAccountDto updatedTokensDto, Account existingAccount) {

        log.trace("Effettuo la modifica di 'tokens'...");

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

        log.trace("'tokens' modificato correttamente.");
    }

    private void ifPresentUpdateIdFacebook(String updatedIdFacebook, TokensAccount existingTokens) {

        log.trace("Effettuo la modifica di 'idFacebook'...");

        if (updatedIdFacebook != null) {
            existingTokens.setIdFacebook(updatedIdFacebook);
        }

        log.trace("'idFacebook' modificato correttamente.");
    }

    private void ifPresentUpdateIdGoogle(String updatedIdGoogle, TokensAccount existingTokens) {

        log.trace("Effettuo la modifica di 'idGoogle'...");

        if (updatedIdGoogle != null) {
            existingTokens.setIdGoogle(updatedIdGoogle);
        }

        log.trace("'idGoogle' modificato correttamente.");
    }

    private void ifPresentUpdateIdX(String updatedIdX, TokensAccount existingTokens) {

        log.trace("Effettuo la modifica di 'idX'...");

        if (updatedIdX != null) {
            existingTokens.setIdX(updatedIdX);
        }

        log.trace("'idX' modificato correttamente.");
    }

    private void ifPresentUpdateIdGitHub(String updatedIdGitHub, TokensAccount existingTokens) {

        log.trace("Effettuo la modifica di 'idGitHub'...");

        if (updatedIdGitHub != null) {
            existingTokens.setIdGitHub(updatedIdGitHub);
        }

        log.trace("'idGitHub' modificato correttamente.");
    }

    private void verifyAccountsProfiloCohesion(Account account, Profilo profiloAccount) throws InvalidParameterException {

        for (Account accountAssociato : profiloAccount.getAccounts()) {
            checkNuovoAccountTypeNotAlreadyPresent(accountAssociato, account);
            checkNuovoAccountCohesionWithOtherAccounts(accountAssociato, account);
        }
    }

    private void checkNuovoAccountTypeNotAlreadyPresent(Account accountAssociato, Account nuovoAccount) throws InvalidParameterException {
        if (accountAssociato != null && nuovoAccount.getClass().equals(accountAssociato.getClass())) {
            log.warn("Non puoi associare l'account con email '{}' a un profilo che è già associato a un account '{}'! Eliminare prima quello precedente.", nuovoAccount.getEmail(), accountAssociato.getClass().getSimpleName());
            throw new InvalidParameterException("Non puoi associare l'account con email '" + nuovoAccount.getEmail() + "' a un profilo che è già associato a un account '" + accountAssociato.getClass().getSimpleName() + "'! Eliminare prima quello precedente.");
        }
    }

    private void checkNuovoAccountCohesionWithOtherAccounts(Account accountAssociato, Account nuovoAccount) throws InvalidParameterException {
        if (accountAssociato != null && !nuovoAccount.getEmail().equals(accountAssociato.getEmail())) {
            log.warn("Non puoi associare l'account con email '{}' a un profilo che è associato a un account '{}' con un'email diversa!", nuovoAccount.getEmail(), accountAssociato.getClass().getSimpleName());
            throw new InvalidParameterException("Non puoi associare l'account con email '" + nuovoAccount.getEmail() + "' a un profilo che è associato a un account '" + accountAssociato.getClass().getSimpleName() + "' con un'email diversa!");
        }
    }

    @Override
    public boolean isLastAccountOfProfilo(Account account) throws InvalidParameterException {
        Profilo profiloAssociato = account.getProfilo();
        if (profiloAssociato == null)
            throw new InvalidParameterException("Il profilo associato all'account '" + account.getIdAccount() + "' è null!");

        return (profiloAssociato.getAccounts().size() == 1);
    }

    @Override
    public void checkEmailNotAlreadyTaken(String email) throws InvalidParameterException {

        log.debug("Verifico che l'email scelta non sia già utilizzata nell'account di altri profili...");

        log.trace("email: {}", email);

        // Recupero la lista di account che hanno la stessa email
        List<Account> foundAccounts = accountRepository.findByEmailIs(email, Pageable.unpaged()).toList();

        log.trace("foundAccounts: {}", foundAccounts);

        // Se l'email è gia stata utilizzata, allora mando l'eccezione
        if (!foundAccounts.isEmpty()) {
            log.warn("Impossibile associare l'email '{}' all'account di questo profilo poichè è già associata all'account di un altro profilo!", email);
            throw new InvalidParameterException("L'email '" + email + "' è già associata all'account di un altro profilo!");
        } else {
            log.debug("L'email non è utilizzata in account di altri profili.");
        }
    }

    @Override
    public void checkEmailNotAlreadyTaken(Account account) throws InvalidParameterException {

        log.debug("Verifico che l'email scelta non sia già utilizzata nell'account di altri profili...");

        log.trace("account: {}", account);

        String email = account.getEmail();

        // Recupero la lista di account che hanno la stessa email
        Page<Account> foundAccounts = accountRepository.findByEmailIs(email, Pageable.unpaged());

        List<Account> accountConEmailGiaAssegnata = new ArrayList<>();
        for (Account foundAccount : foundAccounts) {
            if (!foundAccount.getProfilo().equals(account.getProfilo())) {
                accountConEmailGiaAssegnata.add(foundAccount);
            }
        }

        log.trace("accountConEmailGiaAssegnata: {}", accountConEmailGiaAssegnata);

        // Se l'email è gia stata utilizzata, allora mando l'eccezione
        if (!accountConEmailGiaAssegnata.isEmpty()) {
            log.warn("Impossibile associare l'email '{}' all'account di questo profilo poichè è già associata all'account di un altro profilo!", email);
            throw new InvalidParameterException("L'email '" + email + "' è già associata all'account di un altro profilo!");
        } else {
            log.debug("L'email non è utilizzata in account di altri profili.");
        }
    }
}