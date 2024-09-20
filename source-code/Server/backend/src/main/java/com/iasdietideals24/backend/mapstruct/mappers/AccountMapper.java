package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.InvalidAccountTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    // Shallow DTO
    AccountShallowDto toShallowDto(Account account){
        if (account == null)
            return null;

        AccountShallowDto accountShallowDto = new AccountShallowDto();

        if (account instanceof Compratore) {
            accountShallowDto.setTipoAccount(Compratore.class.getSimpleName());
        } else if (account instanceof Venditore) {
            accountShallowDto.setTipoAccount(Venditore.class.getSimpleName());
        } else {
            accountShallowDto.setTipoAccount(null);
        }

        accountShallowDto.setEmail(account.getEmail());

        return accountShallowDto;
    }

    Account toEntity(AccountShallowDto accountShallowDto) throws InvalidAccountTypeException {
        if (accountShallowDto == null)
            return null;

        if (accountShallowDto.getTipoAccount().equals(Compratore.class.getSimpleName())) {
            return toCompratore(accountShallowDto);
        } else if (accountShallowDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
            return toVenditore(accountShallowDto);
        } else {
            throw new InvalidAccountTypeException();
        }
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "email", target = "email")
    abstract Compratore toCompratore(AccountShallowDto accountShallowDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "email", target = "email")
    abstract Venditore toVenditore(AccountShallowDto accountShallowDto);

    abstract Set<AccountShallowDto> toShallowDto(Set<Account> accounts);

    abstract Set<Account> toEntity(Set<AccountShallowDto> accountsShallowDto) throws InvalidAccountTypeException;
}
