package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ProfiloMapper {

    Profilo toEntity(ProfiloDto profiloDto);

    @InheritConfiguration
    ProfiloDto toDto(Profilo profilo);
}
