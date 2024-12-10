package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AstaSilenziosaService {

    AstaSilenziosaDto create(AstaSilenziosaDto nuovaAstaSilenziosaDto) throws InvalidParameterException;

    Page<AstaSilenziosaDto> findAll(Pageable pageable);

    Page<AstaSilenziosaDto> findByProprietarioIdAccountIs(Long idAccount, Pageable pageable);

    Page<AstaSilenziosaDto> findByNomeLikeAndCategoriaNomeLike(String nomeAsta, String nomeCategoria, Pageable pageable);

    Page<AstaSilenziosaDto> findByOfferenteIdAccountIs(Long idAccount, Pageable pageable);

    Optional<AstaSilenziosaDto> findOne(Long idAsta);

    boolean isExists(Long idAsta);

    AstaSilenziosaDto fullUpdate(Long idAsta, AstaSilenziosaDto updatedAstaSilenziosaDto) throws InvalidParameterException;

    AstaSilenziosaDto partialUpdate(Long idAsta, AstaSilenziosaDto updatedAstaSilenziosaDto) throws InvalidParameterException;

    void delete(Long idAsta);

    void checkFieldsValid(AstaSilenziosaDto astaSilenziosaDto) throws InvalidParameterException;

    void convertRelations(AstaSilenziosaDto astaSilenziosaDto, AstaSilenziosa astaSilenziosa) throws InvalidParameterException;

    void updatePresentFields(AstaSilenziosaDto updatedAstaSilenziosaDto, AstaSilenziosa existingAstaSilenziosa) throws InvalidParameterException;

    void closeAstaSilenziosa(AstaSilenziosa expiredAsta);
}
