package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface NotificaMapper {

    @Mapping(source = "mittente.email", target = "emailMittente")
    @Mapping(source = "destinatari", target = "emailDestinatari", qualifiedByName = "accountsSetToEmailAccountsSet")
    @Mapping(source = "astaAssociata.idAsta", target = "idAstaAssociata")
    NotificaDto toDto(Notifica notifica);

//    @InheritInverseConfiguration
//    Notifica toEntity(NotificaDto notificaDto);

    @Named("accountsSetToEmailAccountsSet")
    default Set<String> accountsSetToEmailAccountsSet(Set<Account> accounts) {
        Set<String> emailAccounts = new HashSet<>();

        for (Account account : accounts) {
            emailAccounts.add(account.getEmail());
        }

        return emailAccounts;
    }
}
