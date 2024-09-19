package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiVenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

import java.util.Set;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface OffertaDiVenditoreMapper {

    @SubclassMapping(source = OffertaInversa.class, target = OffertaInversaDto.class)
    OffertaDiVenditoreDto toDto(OffertaDiVenditore offertaDiVenditore);

    //OffertaDiVenditore toEntity(OffertaDiVenditoreDto offertaDiVenditoreDto);

    Set<OffertaDiVenditoreDto> toDto(Set<OffertaDiVenditore> offertaDiVenditore);

    //Set<OffertaDiVenditore> toEntity(Set<OffertaDiVenditoreDto> offertaDiVenditoreDto);
}
