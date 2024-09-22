package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.SETTER)
public abstract class AccountMapper {

    // Shallow DTO
    public AccountShallowDto toShallowDto(Account account) {
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

    public Account toEntity(AccountShallowDto accountShallowDto) throws InvalidTypeException {
        if (accountShallowDto == null)
            return null;

        if (accountShallowDto.getTipoAccount().equals(Compratore.class.getSimpleName())) {
            return toCompratore(accountShallowDto);
        } else if (accountShallowDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
            return toVenditore(accountShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "email", target = "email")
    public abstract Compratore toCompratore(AccountShallowDto accountShallowDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "email", target = "email")
    public abstract Venditore toVenditore(AccountShallowDto accountShallowDto);

    public abstract Set<AccountShallowDto> toShallowDto(Set<Account> accounts);

    public abstract Set<Account> toEntity(Set<AccountShallowDto> accountsShallowDto) throws InvalidTypeException;
}
