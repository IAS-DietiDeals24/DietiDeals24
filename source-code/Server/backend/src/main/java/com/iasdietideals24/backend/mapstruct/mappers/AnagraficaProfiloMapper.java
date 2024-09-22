package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.mapstruct.dto.utilities.AnagraficaProfiloDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnagraficaProfiloMapper {

    AnagraficaProfiloDto toDto(AnagraficaProfilo anagraficaProfilo);

    @InheritInverseConfiguration
    AnagraficaProfilo toEntity(AnagraficaProfiloDto anagraficaProfiloDto);
}
