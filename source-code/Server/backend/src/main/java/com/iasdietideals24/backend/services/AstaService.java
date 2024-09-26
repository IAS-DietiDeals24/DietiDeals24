package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDto;

public interface AstaService {

    void checkFieldsValid(AstaDto astaDto) throws InvalidParameterException;

    void convertRelations(AstaDto astaDto, Asta asta) throws InvalidParameterException;

    void updatePresentFields(AstaDto updatedAstaDto, Asta existingAsta) throws InvalidParameterException;
}
