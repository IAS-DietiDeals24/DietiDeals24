package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AstaInversaMapper {

    @Mapping(source = "notificheAssociate", target = "idNotificheAssociate", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "proprietario.email", target = "emailProprietario")
    @Mapping(source = "offerteRicevute", target = "idOfferteRicevute", qualifiedByName = "offerteSetToIdOfferteSet")
    AstaInversaDto toDto(AstaInversa astaInversa);

//    @InheritInverseConfiguration
//    AstaInversa toEntity(AstaInversaDto astaInversaDto);

    @Named("notificheSetToIdNotificheSet")
    default Set<Long> notificheSetToIdNotificheSet(Set<Notifica> notifiche) {
        Set<Long> idNotifiche = new HashSet<>();

        for (Notifica notifica : notifiche) {
            idNotifiche.add(notifica.getIdNotifica());
        }

        return idNotifiche;
    }

    @Named("offerteSetToIdOfferteSet")
    default Set<Long> offerteSetToIdOfferteSet(Set<OffertaInversa> offerte) {
        Set<Long> idOfferte = new LinkedHashSet<>();

        for (OffertaInversa offertaInversa : offerte) {
            idOfferte.add(offertaInversa.getIdOfferta());
        }

        return idOfferte;
    }
}
