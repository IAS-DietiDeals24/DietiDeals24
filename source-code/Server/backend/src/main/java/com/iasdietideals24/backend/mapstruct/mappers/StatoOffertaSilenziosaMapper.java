package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.StatoOffertaSilenziosa;
import com.iasdietideals24.backend.mapstruct.dto.utilities.StatoOffertaSilenziosaDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatoOffertaSilenziosaMapper {

    StatoOffertaSilenziosaDto toDto(StatoOffertaSilenziosa statoOffertaSilenziosa);

    @InheritInverseConfiguration
    StatoOffertaSilenziosa toEntity(StatoOffertaSilenziosaDto statoOffertaSilenziosaDto);
}
