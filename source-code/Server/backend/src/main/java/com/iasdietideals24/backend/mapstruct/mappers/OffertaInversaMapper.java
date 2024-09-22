package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, AstaMapper.class})
public interface OffertaInversaMapper {

    @Mapping(source = "venditoreCollegato", target = "venditoreCollegatoShallow")
    @Mapping(source = "astaRiferimento", target = "astaRiferimentoShallow")
    OffertaInversaDto toDto(OffertaInversa offertaInversa);

    @InheritInverseConfiguration
    OffertaInversa toEntity(OffertaInversaDto offertaInversaDto) throws InvalidTypeException;
}
