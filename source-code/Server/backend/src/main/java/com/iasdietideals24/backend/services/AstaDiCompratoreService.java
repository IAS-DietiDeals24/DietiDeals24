package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiCompratoreDto;

public interface AstaDiCompratoreService {

    void checkFieldsValid(AstaDiCompratoreDto astaDiCompratoreDto) throws InvalidParameterException;

    void convertRelations(AstaDiCompratoreDto astaDiCompratoreDto, AstaDiCompratore astaDiCompratore) throws InvalidParameterException;

    void updatePresentFields(AstaDiCompratoreDto updatedAstaDiCompratoreDto, AstaDiCompratore existingAstaDiCompratore) throws InvalidParameterException;
}
