package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface AstaInversaMapper {

    AstaInversaDto toDto(AstaInversa astaInversa);

    @InheritConfiguration
    AstaInversa toEntity(AstaInversaDto astaInversaDto);
}
