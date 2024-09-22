package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.SETTER)
public abstract class AstaMapper {

    // Shallow DTO
    public AstaShallowDto toShallowDto(Asta asta) {
        if (asta == null)
            return null;

        AstaShallowDto astaShallowDto = new AstaShallowDto();

        if (asta instanceof AstaDiCompratore) {
            astaShallowDto.setTipoAstaPerAccount(AstaDiCompratore.class.getSimpleName());
            if (asta instanceof AstaInversa) {
                astaShallowDto.setTipoAstaSpecifica(AstaInversa.class.getSimpleName());
            } else {
                astaShallowDto.setTipoAstaSpecifica(null);
            }
        } else if (asta instanceof AstaDiVenditore) {
            astaShallowDto.setTipoAstaPerAccount(AstaDiVenditore.class.getSimpleName());
            if (asta instanceof AstaSilenziosa) {
                astaShallowDto.setTipoAstaSpecifica(AstaSilenziosa.class.getSimpleName());
            } else if (asta instanceof AstaTempoFisso) {
                astaShallowDto.setTipoAstaSpecifica(AstaTempoFisso.class.getSimpleName());
            } else {
                astaShallowDto.setTipoAstaSpecifica(null);
            }
        } else {
            astaShallowDto.setTipoAstaPerAccount(null);
        }

        astaShallowDto.setIdAsta(asta.getIdAsta());

        return astaShallowDto;
    }

    public Asta toEntity(AstaShallowDto astaShallowDto) throws InvalidTypeException {
        if (astaShallowDto == null)
            return null;

        if (astaShallowDto.getTipoAstaPerAccount().equals(AstaDiCompratore.class.getSimpleName())) {
            return toAstaDiCompratore(astaShallowDto);
        } else if (astaShallowDto.getTipoAstaPerAccount().equals(AstaDiVenditore.class.getSimpleName())) {
            return toAstaDiVenditore(astaShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    public AstaDiCompratore toAstaDiCompratore(AstaShallowDto astaShallowDto) throws InvalidTypeException {
        if (astaShallowDto == null) {
            return null;
        }

        if (astaShallowDto.getTipoAstaSpecifica().equals(AstaInversa.class.getSimpleName())) {
            return toAstaInversa(astaShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    public AstaDiVenditore toAstaDiVenditore(AstaShallowDto astaShallowDto) throws InvalidTypeException {
        if (astaShallowDto == null) {
            return null;
        }

        if (astaShallowDto.getTipoAstaSpecifica().equals(AstaTempoFisso.class.getSimpleName())) {
            return toAstaTempoFisso(astaShallowDto);
        } else if (astaShallowDto.getTipoAstaSpecifica().equals(AstaSilenziosa.class.getSimpleName())) {
            return toAstaSilenziosa(astaShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idAsta", target = "idAsta")
    public abstract AstaInversa toAstaInversa(AstaShallowDto astaShallowDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idAsta", target = "idAsta")
    public abstract AstaTempoFisso toAstaTempoFisso(AstaShallowDto astaShallowDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idAsta", target = "idAsta")
    public abstract AstaSilenziosa toAstaSilenziosa(AstaShallowDto astaShallowDto);

    public abstract Set<AstaShallowDto> toShallowDto(Set<Asta> aste);

    public abstract Set<Asta> toEntity(Set<AstaShallowDto> asteShallowDto) throws InvalidTypeException;
}
