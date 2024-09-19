package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.OffertaDiCompratore;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiCompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

import java.util.Set;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface OffertaDiCompratoreMapper {

    @SubclassMapping(source = OffertaTempoFisso.class, target = OffertaTempoFissoDto.class)
    @SubclassMapping(source = OffertaSilenziosa.class, target = OffertaSilenziosaDto.class)
    OffertaDiCompratoreDto toDto(OffertaDiCompratore offertaDiCompratore);

    OffertaDiCompratore toEntity(OffertaDiCompratoreDto offertaDiCompratoreDto);

    //Set<OffertaDiCompratoreDto> toDto(Set<OffertaDiCompratore> offertaDiCompratore);

    //Set<OffertaDiCompratore> toEntity(Set<OffertaDiCompratoreDto> offertaDiCompratoreDto);
}
