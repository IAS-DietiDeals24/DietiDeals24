package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;

public interface AccountService {

    void checkFieldsValid(AccountDto accountDto) throws InvalidParameterException;

    void convertRelations(AccountDto accountDto, Account account) throws InvalidParameterException;

    void updatePresentFields(AccountDto updatedAccountDto, Account existingAccount) throws InvalidParameterException;

    boolean isLastAccountOfProfilo(Account account) throws InvalidParameterException;

    void checkEmailNotAlreadyTaken(String email) throws InvalidParameterException;

    void checkEmailNotAlreadyTaken(Account account) throws InvalidParameterException;
}
