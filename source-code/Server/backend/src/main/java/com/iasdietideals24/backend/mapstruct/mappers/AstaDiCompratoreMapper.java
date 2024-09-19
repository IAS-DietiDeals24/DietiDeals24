package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiCompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

import java.util.Set;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface AstaDiCompratoreMapper {

    @SubclassMapping(source = AstaInversa.class, target = AstaInversaDto.class)
    AstaDiCompratoreDto toDto(AstaDiCompratore astaDiCompratore);

    //AstaDiCompratore toEntity(AstaDiCompratoreDto astaDiCompratoreDto);

    Set<AstaDiCompratoreDto> toDto(Set<AstaDiCompratore> astaDiCompratore);

   // Set<AstaDiCompratore> toEntity(Set<AstaDiCompratoreDto> astaDiCompratoreDto);
}
