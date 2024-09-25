package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;

public interface AccountService {

    void validateData(AccountDto nuovoAccountDto) throws InvalidParameterException;

    void convertRelations(AccountDto nuovoAccountDto, Account nuovoAccount) throws InvalidParameterException;

    void ifPresentUpdates(AccountDto updatedAccountDto, Account existingAccount) throws InvalidParameterException;
}
