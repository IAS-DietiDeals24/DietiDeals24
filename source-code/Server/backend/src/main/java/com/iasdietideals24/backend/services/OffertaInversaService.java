package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OffertaInversaService {

    OffertaInversaDto create(OffertaInversaDto nuovaOffertaInversaDto) throws InvalidParameterException;

    Page<OffertaInversaDto> findAll(Pageable pageable);

    Optional<OffertaInversaDto> findOne(Long idOfferta);

    boolean isExists(Long idOfferta);

    OffertaInversaDto fullUpdate(Long idOfferta, OffertaInversaDto updatedOffertaInversaDto) throws InvalidParameterException;

    OffertaInversaDto partialUpdate(Long idOfferta, OffertaInversaDto updatedOffertaInversaDto) throws InvalidParameterException;

    void delete(Long idOfferta);

    void checkFieldsValid(OffertaInversaDto offertaInversaDto) throws InvalidParameterException;

    void convertRelations(OffertaInversaDto offertaInversaDto, OffertaInversa offertaInversa) throws InvalidParameterException;

    void updatePresentFields(OffertaInversaDto updatedOffertaInversaDto, OffertaInversa existingOffertaInversa) throws InvalidParameterException;
}
