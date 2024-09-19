package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface OffertaInversaMapper {

    OffertaInversaDto toDto(OffertaInversa offertaInversa);

    @InheritConfiguration
    OffertaInversa toEntity(OffertaInversaDto offertaInversaDto);

    Set<OffertaInversaDto> toDto(Set<OffertaInversa> offertaInversa);

    Set<OffertaInversa> toEntity(Set<OffertaInversaDto> offertaInversaDto);
}
