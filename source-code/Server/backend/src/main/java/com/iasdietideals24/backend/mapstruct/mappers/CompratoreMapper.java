package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface CompratoreMapper {

    CompratoreDto toDto(Compratore compratore);

    @InheritConfiguration
    Compratore toEntity(CompratoreDto compratoreDto);
}
