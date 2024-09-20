package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.InvalidAstaTypeException;
import com.iasdietideals24.backend.exceptions.InvalidOffertaTypeException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProfiloMapper.class, NotificaMapper.class, AstaMapper.class, OffertaMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface VenditoreMapper {

    @Mapping(source = "profilo", target = "profiloShallow")
    @Mapping(source = "notificheInviate", target = "notificheInviateShallow")
    @Mapping(source = "notificheRicevute", target = "notificheRicevuteShallow")
    @Mapping(source = "astePossedute", target = "astePosseduteShallow")
    @Mapping(source = "offerteCollegate", target = "offerteCollegateShallow")
    VenditoreDto toDto(Venditore venditore);

    @InheritInverseConfiguration
    Venditore toEntity(VenditoreDto venditoreDto) throws InvalidAstaTypeException, InvalidOffertaTypeException;
}
