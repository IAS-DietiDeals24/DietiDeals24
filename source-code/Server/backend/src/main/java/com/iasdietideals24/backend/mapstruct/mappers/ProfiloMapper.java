package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidAccountTypeException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AccountEssentialsMapper.class, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ProfiloMapper {

    @Mapping(source = "accounts", target = "accountsEssentials")
    ProfiloDto toDto(Profilo profilo);

    @InheritInverseConfiguration
    Profilo toEntity(ProfiloDto profiloDto) throws InvalidAccountTypeException;

    /* LASCIA COMMENTATO FINCHE' NON FACCIAMO TEST SU toEntity
    @Named("accountsSetToAccountEssentialsDtoSet")
    default Set<AccountEssentialsDto> accountsSetToAccountEssentialsDtoSet(Set<Account> accounts) {
        Set<AccountEssentialsDto> accountEssentialsDtos = new HashSet<>();

        for (Account a : accounts) {
            AccountEssentialsDto aeDto = new AccountEssentialsDto();
            aeDto.setEmail(a.getEmail());
            aeDto.setTipoAccount(a.getClass().getSimpleName());

            accountEssentialsDtos.add(aeDto);
        }

        return accountEssentialsDtos;
    }

    @Named("accountEssentialsDtoSetToAccountsSet")
    default Set<Account> accountEssentialsDtoSetToAccountsSet(Set<AccountEssentialsDto> accountEssentialsDtos) throws InvalidAccountTypeException {
        Set<Account> accounts = new HashSet<>();

        for (AccountEssentialsDto aeDto : accountEssentialsDtos) {
            if (aeDto.getTipoAccount().equals(Compratore.class.getSimpleName())) {
                Compratore c = new Compratore();
                c.setEmail(aeDto.getEmail());
                accounts.add(c);
            } else if (aeDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
                Venditore v = new Venditore();
                v.setEmail(aeDto.getEmail());
                accounts.add(v);
            } else {
                throw new InvalidAccountTypeException();
            }
        }

        return accounts;
    }
    */
}
