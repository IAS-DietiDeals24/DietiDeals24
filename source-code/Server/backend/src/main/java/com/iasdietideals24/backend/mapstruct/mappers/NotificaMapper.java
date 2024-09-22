package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, AstaMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface NotificaMapper {

    @Mapping(source = "mittente", target = "mittenteShallow")
    @Mapping(source = "destinatari", target = "destinatariShallow")
    @Mapping(source = "astaAssociata", target = "astaAssociataShallow")
    NotificaDto toDto(Notifica notifica);

    @InheritInverseConfiguration
    Notifica toEntity(NotificaDto notificaDto) throws InvalidTypeException;

    // Shallow DTO
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idNotifica", target = "idNotifica")
    NotificaShallowDto toShallowDto(Notifica notifica);

    @InheritInverseConfiguration
    Notifica toEntity(NotificaShallowDto notificaShallowDto);

    Set<NotificaShallowDto> toShallowDto(Set<Notifica> notifiche);

    Set<Notifica> toEntity(Set<NotificaShallowDto> notificheShallowDto);
}
