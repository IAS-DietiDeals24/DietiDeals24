package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface StatoAstaMapper {

    String toStringEnum(StatoAsta statoAsta);

    @EnumMapping(unexpectedValueMappingException = InvalidParameterException.class)
    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    StatoAsta toEntity(String statoAstaString) throws InvalidParameterException;
}
