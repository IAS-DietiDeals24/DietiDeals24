package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokensAccountMapper {

    TokensAccountDto toDto(TokensAccount tokensAccount);

    @InheritInverseConfiguration
    TokensAccount toEntity(TokensAccountDto tokensAccountDto);
}
