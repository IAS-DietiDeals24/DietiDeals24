package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface AstaTempoFissoMapper {

    AstaTempoFissoDto toDto(AstaTempoFisso astaTempoFisso);

    @InheritConfiguration
    AstaTempoFisso toEntity(AstaTempoFissoDto astaTempoFissoDto);
}
