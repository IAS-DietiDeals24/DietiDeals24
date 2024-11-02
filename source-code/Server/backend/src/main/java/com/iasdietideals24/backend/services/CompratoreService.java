package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompratoreService {

    CompratoreDto create(String email, CompratoreDto nuovoCompratoreDto) throws InvalidParameterException;

    Page<CompratoreDto> findAll(Pageable pageable);

    Optional<CompratoreDto> findByIdFacebook(String email);

    Optional<CompratoreDto> findOne(String email);

    Optional<CompratoreDto> findOneWithPassword(String email, String password);

    boolean isExists(String email);

    CompratoreDto fullUpdate(String email, CompratoreDto updatedCompratoreDto) throws InvalidParameterException;

    CompratoreDto partialUpdate(String email, CompratoreDto updatedCompratoreDto) throws InvalidParameterException;

    void delete(String email) throws InvalidParameterException;

    void checkFieldsValid(CompratoreDto compratoreDto) throws InvalidParameterException;

    void convertRelations(CompratoreDto compratoreDto, Compratore compratore) throws InvalidParameterException;

    void updatePresentFields(CompratoreDto updatedCompratoreDto, Compratore existingCompratore) throws InvalidParameterException;
}
