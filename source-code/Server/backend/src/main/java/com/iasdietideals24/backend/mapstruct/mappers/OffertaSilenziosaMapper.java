package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OffertaSilenziosaMapper {

    @Mapping(source = "compratoreCollegato.email", target = "emailCompratoreCollegato")
    @Mapping(source = "astaRiferimento.idAsta", target = "idAstaRiferimento")
    OffertaSilenziosaDto toDto(OffertaSilenziosa offertaSilenziosa);

//    @InheritInverseConfiguration
//    OffertaSilenziosa toEntity(OffertaSilenziosaDto offertaSilenziosaDto);
}
