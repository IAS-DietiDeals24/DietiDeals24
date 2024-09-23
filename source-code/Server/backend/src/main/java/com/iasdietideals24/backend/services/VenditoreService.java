package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VenditoreService extends AccountService {


    VenditoreDto create(String email, VenditoreDto nuovoVenditoreDto) throws InvalidParameterException;

    Page<VenditoreDto> findAll(Pageable pageable);

    Optional<VenditoreDto> findOne(String email);

    boolean isExists(String email);

    VenditoreDto fullUpdate(String email, VenditoreDto updatedVenditoreDto) throws InvalidParameterException;

    VenditoreDto partialUpdate(String email, VenditoreDto updatedVenditoreDto) throws InvalidParameterException;

    void delete(String email);

    @Override
    void validateData(AccountDto nuovoVenditoreDto) throws InvalidParameterException;
}
