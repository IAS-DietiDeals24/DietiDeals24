package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AstaTempoFissoMapper {

    @Mapping(source = "notificheAssociate", target = "idNotificheAssociate", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "proprietario.email", target = "emailProprietario")
    @Mapping(source = "offerteRicevute", target = "idOfferteRicevute", qualifiedByName = "offerteSetToIdOfferteSet")
    AstaTempoFissoDto toDto(AstaTempoFisso astaTempoFisso);

//    @InheritInverseConfiguration
//    AstaTempoFisso toEntity(AstaTempoFissoDto astaTempoFissoDto);

    @Named("notificheSetToIdNotificheSet")
    default Set<Long> notificheSetToIdNotificheSet(Set<Notifica> notifiche) {
        Set<Long> idNotifiche = new HashSet<>();

        for (Notifica notifica : notifiche) {
            idNotifiche.add(notifica.getIdNotifica());
        }

        return idNotifiche;
    }

    @Named("offerteSetToIdOfferteSet")
    default Set<Long> offerteSetToIdOfferteSet(Set<OffertaTempoFisso> offerte) {
        Set<Long> idOfferte = new LinkedHashSet<>();

        for (OffertaTempoFisso offertaTempoFisso : offerte) {
            idOfferte.add(offertaTempoFisso.getIdOfferta());
        }

        return idOfferte;
    }
}
