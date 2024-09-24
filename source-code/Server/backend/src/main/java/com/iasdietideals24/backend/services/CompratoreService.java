package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompratoreService extends AccountService {


    CompratoreDto create(String email, CompratoreDto nuovoCompratoreDto) throws InvalidParameterException;

    Page<CompratoreDto> findAll(Pageable pageable);

    Optional<CompratoreDto> findOne(String email);

    boolean isExists(String email);

    CompratoreDto fullUpdate(String email, CompratoreDto updatedCompratoreDto) throws InvalidParameterException;

    CompratoreDto partialUpdate(String email, CompratoreDto updatedCompratoreDto) throws InvalidParameterException;

    void delete(String email);

    @Override
    void validateData(AccountDto nuovoCompratoreDto) throws InvalidParameterException;
}
