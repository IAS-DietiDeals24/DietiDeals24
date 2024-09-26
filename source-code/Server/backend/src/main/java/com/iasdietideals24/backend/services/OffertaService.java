package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDto;

public interface OffertaService {

    void checkFieldsValid(OffertaDto offertaDto) throws InvalidParameterException;

    void convertRelations(OffertaDto offertaDto, Offerta offerta) throws InvalidParameterException;

    void updatePresentFields(OffertaDto updatedOffertaDto, Offerta existingOfferta) throws InvalidParameterException;
}
