package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OffertaSilenziosaService {

    OffertaSilenziosaDto create(OffertaSilenziosaDto nuovaOffertaSilenziosaDto) throws InvalidParameterException;

    Page<OffertaSilenziosaDto> findAll(Pageable pageable);

    Optional<OffertaSilenziosaDto> findOne(Long idOfferta);

    boolean isExists(Long idOfferta);

    OffertaSilenziosaDto fullUpdate(Long idOfferta, OffertaSilenziosaDto updatedOffertaSilenziosaDto) throws InvalidParameterException;

    OffertaSilenziosaDto partialUpdate(Long idOfferta, OffertaSilenziosaDto updatedOffertaSilenziosaDto) throws InvalidParameterException;

    void delete(Long idOfferta);

    void checkFieldsValid(OffertaSilenziosaDto offertaSilenziosaDto) throws InvalidParameterException;

    void convertRelations(OffertaSilenziosaDto offertaSilenziosaDto, OffertaSilenziosa offertaSilenziosa) throws InvalidParameterException;

    void updatePresentFields(OffertaSilenziosaDto updatedOffertaSilenziosaDto, OffertaSilenziosa existingOffertaSilenziosa) throws InvalidParameterException;
}
