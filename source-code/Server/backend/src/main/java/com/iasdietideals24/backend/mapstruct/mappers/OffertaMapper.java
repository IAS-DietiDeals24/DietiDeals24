package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.SETTER)
public abstract class OffertaMapper {

    // Shallow DTO
    public OffertaShallowDto toShallowDto(Offerta offerta) {
        if (offerta == null)
            return null;

        OffertaShallowDto offertaShallowDto = new OffertaShallowDto();

        if (offerta instanceof OffertaDiCompratore) {
            offertaShallowDto.setTipoOffertaPerAccount(OffertaDiCompratore.class.getSimpleName());
            if (offerta instanceof OffertaSilenziosa) {
                offertaShallowDto.setTipoOffertaSpecifica(OffertaSilenziosa.class.getSimpleName());
            } else if (offerta instanceof OffertaTempoFisso) {
                offertaShallowDto.setTipoOffertaSpecifica(OffertaTempoFisso.class.getSimpleName());
            } else {
                offertaShallowDto.setTipoOffertaSpecifica(null);
            }
        } else if (offerta instanceof OffertaDiVenditore) {
            offertaShallowDto.setTipoOffertaPerAccount(OffertaDiVenditore.class.getSimpleName());
            if (offerta instanceof OffertaInversa) {
                offertaShallowDto.setTipoOffertaSpecifica(OffertaInversa.class.getSimpleName());
            } else {
                offertaShallowDto.setTipoOffertaSpecifica(null);
            }
        } else {
            offertaShallowDto.setTipoOffertaPerAccount(null);
        }

        offertaShallowDto.setIdOfferta(offerta.getIdOfferta());

        return offertaShallowDto;
    }

    public Offerta toEntity(OffertaShallowDto offertaShallowDto) throws InvalidTypeException {
        if (offertaShallowDto == null)
            return null;

        if (offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiCompratore.class.getSimpleName())) {
            return toOffertaDiCompratore(offertaShallowDto);
        } else if (offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiVenditore.class.getSimpleName())) {
            return toOffertaDiVenditore(offertaShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    public OffertaDiVenditore toOffertaDiVenditore(OffertaShallowDto offertaShallowDto) throws InvalidTypeException {
        if (offertaShallowDto == null)
            return null;

        if (offertaShallowDto.getTipoOffertaSpecifica().equals(OffertaInversa.class.getSimpleName())) {
            return toOffertaInversa(offertaShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    public OffertaDiCompratore toOffertaDiCompratore(OffertaShallowDto offertaShallowDto) throws InvalidTypeException {
        if (offertaShallowDto == null)
            return null;

        if (offertaShallowDto.getTipoOffertaSpecifica().equals(OffertaTempoFisso.class.getSimpleName())) {
            return toOffertaTempoFisso(offertaShallowDto);
        } else if (offertaShallowDto.getTipoOffertaSpecifica().equals(OffertaSilenziosa.class.getSimpleName())) {
            return toOffertaSilenziosa(offertaShallowDto);
        } else {
            throw new InvalidTypeException();
        }
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idOfferta", target = "idOfferta")
    public abstract OffertaInversa toOffertaInversa(OffertaShallowDto offertaShallowDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idOfferta", target = "idOfferta")
    public abstract OffertaTempoFisso toOffertaTempoFisso(OffertaShallowDto offertaShallowDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "idOfferta", target = "idOfferta")
    public abstract OffertaSilenziosa toOffertaSilenziosa(OffertaShallowDto offertaShallowDto);

    public abstract Set<OffertaShallowDto> toShallowDto(Set<Offerta> offerta);

    public abstract Set<Offerta> toEntity(Set<OffertaShallowDto> offertaShallowDto) throws InvalidTypeException;
}
