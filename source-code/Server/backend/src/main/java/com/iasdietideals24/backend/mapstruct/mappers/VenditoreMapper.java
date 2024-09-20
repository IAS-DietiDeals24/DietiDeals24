package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface VenditoreMapper {

    @Mapping(source = "profilo.nomeUtente", target = "nomeUtenteProfilo")
    @Mapping(source = "notificheInviate", target = "idNotificheInviate", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "notificheRicevute", target = "idNotificheRicevute", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "astePossedute", target = "idAstePossedute", qualifiedByName = "asteSetToIdAsteSet")
    @Mapping(source = "offerteCollegate", target = "idOfferteCollegate", qualifiedByName = "offerteSetToIdOfferteSet")
    VenditoreDto toDto(Venditore venditore);

//    @InheritInverseConfiguration
//    Venditore toEntity(VenditoreDto venditoreDto);

    @Named("notificheSetToIdNotificheSet")
    default Set<Long> notificheSetToIdNotificheSet(Set<Notifica> notifiche) {
        Set<Long> idNotifiche = new HashSet<>();

        for (Notifica notifica : notifiche) {
            idNotifiche.add(notifica.getIdNotifica());
        }

        return idNotifiche;
    }

    @Named("asteSetToIdAsteSet")
    default Set<Long> asteSetToIdAsteSet(Set<AstaDiVenditore> aste) {
        Set<Long> idAste = new LinkedHashSet<>();

        for (AstaDiVenditore astaDiVenditore : aste) {
            idAste.add(astaDiVenditore.getIdAsta());
        }

        return idAste;
    }

    @Named("offerteSetToIdOfferteSet")
    default Set<Long> offerteSetToIdOfferteSet(Set<OffertaDiVenditore> offerte) {
        Set<Long> idOfferte = new LinkedHashSet<>();

        for (OffertaDiVenditore offertaDiVenditore : offerte) {
            idOfferte.add(offertaDiVenditore.getIdOfferta());
        }

        return idOfferte;
    }
}
