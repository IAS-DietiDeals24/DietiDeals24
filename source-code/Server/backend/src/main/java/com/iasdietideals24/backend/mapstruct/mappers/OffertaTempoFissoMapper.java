package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, AstaMapper.class})
public interface OffertaTempoFissoMapper {

    @Mapping(source = "compratoreCollegato", target = "compratoreCollegatoShallow")
    @Mapping(source = "astaRiferimento", target = "astaRiferimentoShallow")
    OffertaTempoFissoDto toDto(OffertaTempoFisso offertaTempoFisso);

    @InheritInverseConfiguration
    OffertaTempoFisso toEntity(OffertaTempoFissoDto offertaTempoFissoDto) throws InvalidTypeException;
}
