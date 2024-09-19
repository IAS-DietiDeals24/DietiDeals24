package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.entities.OffertaDiCompratore;
import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiCompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiVenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface OffertaMapper {

    @SubclassMapping(source = OffertaDiCompratore.class, target = OffertaDiCompratoreDto.class)
    @SubclassMapping(source = OffertaDiVenditore.class, target = OffertaDiVenditoreDto.class)
    OffertaDto toDto(Offerta offerta);

    //Offerta toEntity(OffertaDto offertaDto);
}
