package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidAccountTypeException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AccountMapper.class, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ProfiloMapper {

    @Mapping(source = "accounts", target = "accountsShallow")
    ProfiloDto toDto(Profilo profilo);

    @InheritInverseConfiguration
    Profilo toEntity(ProfiloDto profiloDto) throws InvalidAccountTypeException;

    // Shallow DTO
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "nomeUtente", target = "nomeUtente")
    ProfiloShallowDto toShallowDto(Profilo profilo);

    @InheritInverseConfiguration
    Profilo toEntity(ProfiloShallowDto profiloShallowDto);
}
