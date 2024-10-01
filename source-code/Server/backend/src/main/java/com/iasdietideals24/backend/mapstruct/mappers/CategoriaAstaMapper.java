package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = AstaMapper.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface CategoriaAstaMapper {

    @Mapping(source = "asteAssegnate", target = "asteAssegnateShallow")
    CategoriaAstaDto toDto(CategoriaAsta categoriaAsta);

    @InheritInverseConfiguration
    CategoriaAsta toEntity(CategoriaAstaDto categoriaAstaDto) throws InvalidTypeException;
}
