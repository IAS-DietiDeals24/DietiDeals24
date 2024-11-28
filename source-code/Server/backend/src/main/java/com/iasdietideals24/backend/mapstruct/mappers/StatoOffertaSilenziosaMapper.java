package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.StatoOffertaSilenziosa;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface StatoOffertaSilenziosaMapper {

    String toStringEnum(StatoOffertaSilenziosa statoOffertaSilenziosa);

    @EnumMapping(unexpectedValueMappingException = InvalidParameterException.class)
    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    StatoOffertaSilenziosa toEntity(String statoOffertaSilenziosaString) throws InvalidParameterException;
}
