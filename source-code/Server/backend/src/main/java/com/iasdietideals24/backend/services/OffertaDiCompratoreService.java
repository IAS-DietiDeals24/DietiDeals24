package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.OffertaDiCompratore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiCompratoreDto;

public interface OffertaDiCompratoreService {

    void checkFieldsValid(OffertaDiCompratoreDto offertaDiCompratoreDto) throws InvalidParameterException;

    void convertRelations(OffertaDiCompratoreDto offertaDiCompratoreDto, OffertaDiCompratore offertaDiCompratore) throws InvalidParameterException;

    void updatePresentFields(OffertaDiCompratoreDto updatedOffertaDiCompratoreDto, OffertaDiCompratore existingOffertaDiCompratore) throws InvalidParameterException;
}
