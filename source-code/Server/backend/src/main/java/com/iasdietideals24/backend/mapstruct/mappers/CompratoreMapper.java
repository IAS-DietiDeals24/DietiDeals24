package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompratoreMapper {

    @Mapping(source = "profilo.nomeUtente", target = "nomeUtenteProfilo")
    @Mapping(source = "notificheInviate", target = "idNotificheInviate", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "notificheRicevute", target = "idNotificheRicevute", qualifiedByName = "notificheSetToIdNotificheSet")
    @Mapping(source = "astePossedute", target = "idAstePossedute", qualifiedByName = "asteSetToIdAsteSet")
    @Mapping(source = "offerteCollegate", target = "idOfferteCollegate", qualifiedByName = "offerteSetToIdOfferteSet")
    CompratoreDto toDto(Compratore compratore);

//    @InheritInverseConfiguration
//    Compratore toEntity(CompratoreDto compratoreDto);

    @Named("notificheSetToIdNotificheSet")
    default Set<Long> notificheSetToIdNotificheSet(Set<Notifica> notifiche) {
        Set<Long> idNotifiche = new HashSet<>();

        for (Notifica notifica : notifiche) {
            idNotifiche.add(notifica.getIdNotifica());
        }

        return idNotifiche;
    }

    @Named("asteSetToIdAsteSet")
    default Set<Long> asteSetToIdAsteSet(Set<AstaDiCompratore> aste) {
        Set<Long> idAste = new LinkedHashSet<>();

        for (AstaDiCompratore astaDiCompratore : aste) {
            idAste.add(astaDiCompratore.getIdAsta());
        }

        return idAste;
    }

    @Named("offerteSetToIdOfferteSet")
    default Set<Long> offerteSetToIdOfferteSet(Set<OffertaDiCompratore> offerte) {
        Set<Long> idOfferte = new LinkedHashSet<>();

        for (OffertaDiCompratore offertaDiCompratore : offerte) {
            idOfferte.add(offertaDiCompratore.getIdOfferta());
        }

        return idOfferte;
    }
}
