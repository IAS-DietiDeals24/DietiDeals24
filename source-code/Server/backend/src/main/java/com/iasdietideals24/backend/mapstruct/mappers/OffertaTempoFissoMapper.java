package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface OffertaTempoFissoMapper {

    OffertaTempoFissoDto toDto(OffertaTempoFisso offertaTempoFisso);

    @InheritConfiguration
    OffertaTempoFisso toEntity(OffertaTempoFissoDto offertaTempoFissoDto);

    Set<OffertaTempoFissoDto> toDto(Set<OffertaTempoFisso> offertaTempoFisso);

    Set<OffertaTempoFisso> toEntity(Set<OffertaTempoFissoDto> offertaTempoFissoDto);
}
