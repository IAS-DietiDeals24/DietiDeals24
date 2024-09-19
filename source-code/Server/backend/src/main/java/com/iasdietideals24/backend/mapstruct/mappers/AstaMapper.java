package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.mapstruct.dto.*;
import org.mapstruct.*;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface AstaMapper {

    @SubclassMapping(source = AstaDiCompratore.class, target = AstaDiCompratoreDto.class)
    @SubclassMapping(source = AstaDiVenditore.class, target = AstaDiVenditoreDto.class)
    AstaDto toDto(Asta asta);

    //Asta toEntity(AstaDto astaDto);
}
