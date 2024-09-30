package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.CategoriaAsta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface CategoriaAstaMapper {

    String toString(CategoriaAsta categoriaAsta);

    @EnumMapping(unexpectedValueMappingException = InvalidParameterException.class)
    @ValueMapping (source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    CategoriaAsta toEntity(String categoriaAstaString) throws InvalidParameterException;
}
