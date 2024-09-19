package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface NotificaMapper {

    NotificaDto toDto(Notifica notifica);

    @InheritConfiguration
    Notifica toEntity(NotificaDto notificaDto);

    Set<NotificaDto> toDto(Set<Notifica> notifiche);

    Set<Notifica> toEntity(Set<NotificaDto> notificheDto);
}
