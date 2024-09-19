package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface OffertaSilenziosaMapper {

    OffertaSilenziosaDto toDto(OffertaSilenziosa offertaSilenziosa);

    @InheritConfiguration
    OffertaSilenziosa toEntity(OffertaSilenziosaDto offertaSilenziosaDto);

    Set<OffertaSilenziosaDto> toDto(Set<OffertaSilenziosa> offertaSilenziosa);

    Set<OffertaSilenziosa> toEntity(Set<OffertaSilenziosaDto> offertaSilenziosaDto);
}
