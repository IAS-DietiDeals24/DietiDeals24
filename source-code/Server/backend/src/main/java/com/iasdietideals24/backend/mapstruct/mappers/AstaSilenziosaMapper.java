package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, NotificaMapper.class, OffertaMapper.class, CategoriaAstaMapper.class, StatoAstaMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface AstaSilenziosaMapper {

    @Mapping(source = "categoria", target = "categoriaShallow")
    @Mapping(source = "notificheAssociate", target = "notificheAssociateShallow")
    @Mapping(source = "proprietario", target = "proprietarioShallow")
    @Mapping(source = "offerteRicevute", target = "offerteRicevuteShallow")
    AstaSilenziosaDto toDto(AstaSilenziosa astaSilenziosa);

    @InheritInverseConfiguration
    AstaSilenziosa toEntity(AstaSilenziosaDto astaSilenziosaDto) throws InvalidTypeException;
}
