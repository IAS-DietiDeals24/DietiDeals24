package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;

public interface AccountService {

    void validateData(AccountDto nuovoAccountDto) throws InvalidParameterException;
}
