package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface AstaSilenziosaMapper {

    AstaSilenziosaDto toDto(AstaSilenziosa astaSilenziosa);

    @InheritConfiguration
    AstaSilenziosa toEntity(AstaSilenziosaDto astaSilenziosaDto);
}
