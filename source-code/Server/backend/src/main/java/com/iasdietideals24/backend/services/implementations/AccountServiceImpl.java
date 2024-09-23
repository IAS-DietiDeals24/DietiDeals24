package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.services.AccountService;

public class AccountServiceImpl implements AccountService {
    @Override
    public void validateData(AccountDto nuovoAccountDto) throws InvalidParameterException {
        isEmailValid(nuovoAccountDto.getEmail());
        isPasswordValid(nuovoAccountDto.getPassword());
        isProfiloShallowValid(nuovoAccountDto.getProfiloShallow());
    }

    protected void isEmailValid(String email) throws InvalidParameterException {
        if (email == null)
            throw new InvalidParameterException("L'email non può essere null!");
        else if (email.isBlank())
            throw new InvalidParameterException("L'email non può essere vuota!");
        else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidParameterException("Formato email non valido!");
    }

    protected void isPasswordValid(String password) throws InvalidParameterException {
        if (password == null)
            throw new InvalidParameterException("La password non può essere null!");
        else if (password.isBlank())
            throw new InvalidParameterException("La password non può essere vuota!");
    }

    protected void isProfiloShallowValid(ProfiloShallowDto profiloShallow) throws InvalidParameterException {
        if (profiloShallow == null)
            throw new InvalidParameterException();

        isNomeUtenteValid(profiloShallow.getNomeUtente());
    }

    protected void isNomeUtenteValid(String nomeUtente) throws InvalidParameterException {
        if (nomeUtente == null)
            throw new InvalidParameterException("Il nome utente non può essere null!");
        else if (nomeUtente.isBlank())
            throw new InvalidParameterException("Il nome utente non può essere vuoto!");
    }
}
