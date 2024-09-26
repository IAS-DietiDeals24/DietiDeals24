package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiVenditoreDto;

public interface OffertaDiVenditoreService {

    void checkFieldsValid(OffertaDiVenditoreDto offertaDiVenditoreDto) throws InvalidParameterException;

    void convertRelations(OffertaDiVenditoreDto offertaDiVenditoreDto, OffertaDiVenditore offertaDiVenditore) throws InvalidParameterException;

    void updatePresentFields(OffertaDiVenditoreDto updatedOffertaDiVenditoreDto, OffertaDiVenditore existingOffertaDiVenditore) throws InvalidParameterException;
}
