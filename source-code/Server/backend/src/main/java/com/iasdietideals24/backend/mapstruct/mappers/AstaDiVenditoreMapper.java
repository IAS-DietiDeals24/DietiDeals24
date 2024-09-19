package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.mapstruct.dto.*;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

import java.util.Set;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface AstaDiVenditoreMapper {

    @SubclassMapping(source = AstaTempoFisso.class, target = AstaTempoFissoDto.class)
    @SubclassMapping(source = AstaSilenziosa.class, target = AstaSilenziosaDto.class)
    AstaDiVenditoreDto toDto(AstaDiVenditore astaDiVenditore);

    //AstaDiVenditore toEntity(AstaDiVenditoreDto astaDiVenditoreDto);

    Set<AstaDiVenditoreDto> toDto(Set<AstaDiVenditore> astaDiVenditore);

    //Set<AstaDiVenditore> toEntity(Set<AstaDiVenditoreDto> astaDiVenditoreDto);
}
