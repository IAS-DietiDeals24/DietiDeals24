package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, AstaMapper.class})
public interface OffertaSilenziosaMapper {

    @Mapping(source = "compratoreCollegato", target = "compratoreCollegatoShallow")
    @Mapping(source = "astaRiferimento", target = "astaRiferimentoShallow")
    OffertaSilenziosaDto toDto(OffertaSilenziosa offertaSilenziosa);

    @InheritInverseConfiguration
    OffertaSilenziosa toEntity(OffertaSilenziosaDto offertaSilenziosaDto) throws InvalidTypeException;
}
