package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface AccountMapper {

    @SubclassMapping(source = CompratoreDto.class, target = Compratore.class)
    @SubclassMapping(source = VenditoreDto.class, target = Venditore.class)
    Account toEntity(AccountDto accountDto);

    @BeanMapping(resultType = CompratoreDto.class)
    AccountDto toCompratoreDto(Account account);

    @BeanMapping(resultType = VenditoreDto.class)
    AccountDto toVenditoreDto(Account account);

    Set<AccountDto> toDto(Set<Account> accounts);

    //Set<Account> toEntity(Set<AccountDto> accountsDto);
}
