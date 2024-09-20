package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.InvalidAccountTypeException;
import com.iasdietideals24.backend.mapstruct.dto.AccountEssentialsDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AccountEssentialsMapper {

    @Mapping(target = "tipoAccount", expression = "java(this.getClass().getSimpleName())")
    abstract AccountEssentialsDto toDto(Account account);

    Account toEntity(AccountEssentialsDto accountEssentialsDto) throws InvalidAccountTypeException {
        if (accountEssentialsDto.getTipoAccount().equals(Compratore.class.getSimpleName())) {
            return toCompratore(accountEssentialsDto);
        } else if (accountEssentialsDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
            return toVenditore(accountEssentialsDto);
        } else {
            throw new InvalidAccountTypeException();
        }
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "email", target = "email")
    abstract Compratore toCompratore(AccountEssentialsDto accountEssentialsDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "email", target = "email")
    abstract Venditore toVenditore(AccountEssentialsDto accountEssentialsDto);

    abstract Set<AccountEssentialsDto> toDto(Set<Account> account);

    abstract Set<Account> toEntity(Set<AccountEssentialsDto> accountEssentialsDto) throws InvalidAccountTypeException;
}
