package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AstaSilenziosaMapper {

    @Mapping(source = "notificheAssociate", target = "idNotificheAssociate", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "proprietario.email", target = "emailProprietario")
    @Mapping(source = "offerteRicevute", target = "idOfferteRicevute", qualifiedByName = "offerteSetToIdOfferteSet")
    AstaSilenziosaDto toDto(AstaSilenziosa astaSilenziosa);

//    @InheritInverseConfiguration
//    AstaSilenziosa toEntity(AstaSilenziosaDto astaSilenziosaDto);

    @Named("notificheSetToIdNotificheSet")
    default Set<Long> notificheSetToIdNotificheSet(Set<Notifica> notifiche) {
        Set<Long> idNotifiche = new HashSet<>();

        for (Notifica notifica : notifiche) {
            idNotifiche.add(notifica.getIdNotifica());
        }

        return idNotifiche;
    }

    @Named("offerteSetToIdOfferteSet")
    default Set<Long> offerteSetToIdOfferteSet(Set<OffertaSilenziosa> offerte) {
        Set<Long> idOfferte = new LinkedHashSet<>();

        for (OffertaSilenziosa offertaSilenziosa : offerte) {
            idOfferte.add(offertaSilenziosa.getIdOfferta());
        }

        return idOfferte;
    }
}
