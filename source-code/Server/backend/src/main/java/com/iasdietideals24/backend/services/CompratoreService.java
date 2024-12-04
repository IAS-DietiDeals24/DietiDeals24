package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompratoreService {

    CompratoreDto create(CompratoreDto nuovoCompratoreDto) throws InvalidParameterException;

    Page<CompratoreDto> findAll(Pageable pageable);

    Optional<CompratoreDto> findOne(Long idAccount);

    Page<CompratoreDto> findByEmailIs(String email, Pageable pageable);

    boolean isExists(Long idAccount);

    CompratoreDto fullUpdate(Long idAccount, CompratoreDto updatedCompratoreDto) throws InvalidParameterException;

    CompratoreDto partialUpdate(Long idAccount, CompratoreDto updatedCompratoreDto) throws InvalidParameterException;

    void delete(Long idAccount) throws InvalidParameterException;

    void checkFieldsValid(CompratoreDto compratoreDto) throws InvalidParameterException;

    void convertRelations(CompratoreDto compratoreDto, Compratore compratore) throws InvalidParameterException;

    void updatePresentFields(CompratoreDto updatedCompratoreDto, Compratore existingCompratore) throws InvalidParameterException;
}
