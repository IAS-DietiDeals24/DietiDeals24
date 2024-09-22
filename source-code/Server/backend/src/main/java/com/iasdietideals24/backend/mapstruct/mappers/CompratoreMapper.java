package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ProfiloMapper.class, NotificaMapper.class, AstaMapper.class, OffertaMapper.class, TokensAccountMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface CompratoreMapper {

    @Mapping(source = "profilo", target = "profiloShallow")
    @Mapping(source = "notificheInviate", target = "notificheInviateShallow")
    @Mapping(source = "notificheRicevute", target = "notificheRicevuteShallow")
    @Mapping(source = "astePossedute", target = "astePosseduteShallow")
    @Mapping(source = "offerteCollegate", target = "offerteCollegateShallow")
    CompratoreDto toDto(Compratore compratore);

    @InheritInverseConfiguration
    Compratore toEntity(CompratoreDto compratoreDto) throws InvalidTypeException;
}
