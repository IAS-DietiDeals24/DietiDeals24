package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, NotificaMapper.class, OffertaMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface AstaTempoFissoMapper {

    @Mapping(source = "notificheAssociate", target = "notificheAssociateShallow")
    @Mapping(source = "proprietario", target = "proprietarioShallow")
    @Mapping(source = "offerteRicevute", target = "offerteRicevuteShallow")
    AstaTempoFissoDto toDto(AstaTempoFisso astaTempoFisso);

    @InheritInverseConfiguration
    AstaTempoFisso toEntity(AstaTempoFissoDto astaTempoFissoDto) throws InvalidTypeException;
}
