package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.AstaDiVenditore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiVenditoreDto;

public interface AstaDiVenditoreService {

    void checkFieldsValid(AstaDiVenditoreDto astaDiVenditoreDto) throws InvalidParameterException;

    void convertRelations(AstaDiVenditoreDto astaDiVenditoreDto, AstaDiVenditore astaDiVenditore) throws InvalidParameterException;

    void updatePresentFields(AstaDiVenditoreDto updatedAstaDiVenditoreDto, AstaDiVenditore existingAstaDiVenditore) throws InvalidParameterException;
}
