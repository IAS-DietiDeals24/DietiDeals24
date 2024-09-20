package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OffertaTempoFissoMapper {

    @Mapping(source = "compratoreCollegato.email", target = "emailCompratoreCollegato")
    @Mapping(source = "astaRiferimento.idAsta", target = "idAstaRiferimento")
    OffertaTempoFissoDto toDto(OffertaTempoFisso offertaTempoFisso);

//    @InheritInverseConfiguration
//    OffertaTempoFisso toEntity(OffertaTempoFissoDto offertaTempoFissoDto);
}
