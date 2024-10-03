package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.CategoriaAstaShallowDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",
        uses = AstaMapper.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface CategoriaAstaMapper {

    @Mapping(source = "asteAssegnate", target = "asteAssegnateShallow")
    CategoriaAstaDto toDto(CategoriaAsta categoriaAsta);

    @InheritInverseConfiguration
    CategoriaAsta toEntity(CategoriaAstaDto categoriaAstaDto) throws InvalidTypeException;

    // Shallow DTO
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "nome", target = "nome")
    CategoriaAstaShallowDto toShallowDto(CategoriaAsta categoriaAsta);

    @InheritInverseConfiguration
    CategoriaAsta toEntity(CategoriaAstaShallowDto categoriaAstaShallowDto);

    Set<CategoriaAstaShallowDto> toShallowDto(Set<CategoriaAsta> categorieAsta);

    Set<CategoriaAsta> toEntity(Set<CategoriaAstaShallowDto> categorieAstaShallowDto);
}
