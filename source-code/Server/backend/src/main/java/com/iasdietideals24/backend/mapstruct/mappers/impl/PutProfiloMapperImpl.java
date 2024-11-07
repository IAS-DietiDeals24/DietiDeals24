package com.iasdietideals24.backend.mapstruct.mappers.impl;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.mapstruct.mappers.AnagraficaProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.LinksProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.PutProfiloMapper;
import com.iasdietideals24.backend.mapstruct.mappers.TokensAccountMapper;
import org.springframework.stereotype.Component;

@Component
public class PutProfiloMapperImpl implements PutProfiloMapper {

    private final AnagraficaProfiloMapper anagraficaProfiloMapper;
    private final TokensAccountMapper tokensAccountMapper;
    private final LinksProfiloMapper linksProfiloMapper;

    public PutProfiloMapperImpl(AnagraficaProfiloMapper anagraficaProfiloMapper, TokensAccountMapper tokensAccountMapper, LinksProfiloMapper linksProfiloMapper) {
        this.anagraficaProfiloMapper = anagraficaProfiloMapper;
        this.tokensAccountMapper = tokensAccountMapper;
        this.linksProfiloMapper = linksProfiloMapper;
    }

    // PutProfiloDto
    public Profilo toEntity(PutProfiloDto putProfiloDto) throws InvalidTypeException {

        if (putProfiloDto == null) {
            return null;
        }

        AnagraficaProfilo anagraficaProfilo = anagraficaProfiloMapper.toEntity(putProfiloDto.getAnagrafica());
        LinksProfilo linksProfilo = linksProfiloMapper.toEntity(putProfiloDto.getLinks());
        TokensAccount tokensAccount = tokensAccountMapper.toEntity(putProfiloDto.getTokens());

        return new Profilo(
                putProfiloDto.getNomeUtente(),
                putProfiloDto.getProfilePicture(),
                anagraficaProfilo,
                linksProfilo,
                putProfiloDto.getEmail(),
                putProfiloDto.getPassword(),
                tokensAccount,
                putProfiloDto.getTipoAccount()
        );
    }
}
