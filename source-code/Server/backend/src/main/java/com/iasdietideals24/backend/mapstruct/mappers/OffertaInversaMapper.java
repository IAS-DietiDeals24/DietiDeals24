package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OffertaInversaMapper {

    @Mapping(source = "venditoreCollegato.email", target = "emailVenditoreCollegato")
    @Mapping(source = "astaRiferimento.idAsta", target = "idAstaRiferimento")
    OffertaInversaDto toDto(OffertaInversa offertaInversa);

    @InheritInverseConfiguration
    OffertaInversa toEntity(OffertaInversaDto offertaInversaDto);
}
