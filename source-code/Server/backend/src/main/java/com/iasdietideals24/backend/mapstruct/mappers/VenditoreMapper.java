package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface VenditoreMapper {

    VenditoreDto toDto(Venditore venditore);

    @InheritConfiguration
    Venditore toEntity(VenditoreDto venditoreDto);
}
