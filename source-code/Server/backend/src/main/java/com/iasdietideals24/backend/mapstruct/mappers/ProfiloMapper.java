package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, AnagraficaProfiloMapper.class, LinksProfiloMapper.class, TokensAccountMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        injectionStrategy = InjectionStrategy.SETTER)
public abstract class ProfiloMapper {

    private AnagraficaProfiloMapper anagraficaProfiloMapper;
    private TokensAccountMapper tokensAccountMapper;
    private LinksProfiloMapper linksProfiloMapper;

    @Mapping(source = "accounts", target = "accountsShallow")
    public abstract ProfiloDto toDto(Profilo profilo);

    @InheritInverseConfiguration
    public abstract Profilo toEntity(ProfiloDto profiloDto) throws InvalidTypeException;

    // Shallow DTO
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "nomeUtente", target = "nomeUtente")
    public abstract ProfiloShallowDto toShallowDto(Profilo profilo);

    @InheritInverseConfiguration
    public abstract Profilo toEntity(ProfiloShallowDto profiloShallowDto);

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
