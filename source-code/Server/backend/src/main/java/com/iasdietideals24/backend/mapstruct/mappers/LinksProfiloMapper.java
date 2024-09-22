package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.mapstruct.dto.utilities.LinksProfiloDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LinksProfiloMapper {

    LinksProfiloDto toDto(LinksProfilo linksProfilo);

    @InheritInverseConfiguration
    LinksProfilo toEntity(LinksProfiloDto linksProfiloDto);
}
