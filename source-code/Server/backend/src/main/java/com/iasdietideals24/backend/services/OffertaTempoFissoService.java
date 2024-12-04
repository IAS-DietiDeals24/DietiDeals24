package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OffertaTempoFissoService {

    OffertaTempoFissoDto create(OffertaTempoFissoDto nuovaOffertaTempoFissoDto) throws InvalidParameterException;

    Page<OffertaTempoFissoDto> findAll(Pageable pageable);

    Page<OffertaTempoFissoDto> findByAstaRiferimentoIdAsta(Long idAsta, Pageable pageable);

    Optional<OffertaTempoFissoDto> findOne(Long idOfferta);

    Optional<OffertaTempoFissoDto> findMaxByValoreAndAstaRiferimentoIdAstaIs(Long idAsta);

    Optional<OffertaTempoFissoDto> findMaxByValoreAndAstaRiferimentoIdAstaIsAndCompratoreCollegatoIdAccountIs(Long idAsta, Long idAccount);

    boolean isExists(Long idOfferta);

    OffertaTempoFissoDto fullUpdate(Long idOfferta, OffertaTempoFissoDto updatedOffertaTempoFissoDto) throws InvalidParameterException;

    OffertaTempoFissoDto partialUpdate(Long idOfferta, OffertaTempoFissoDto updatedOffertaTempoFissoDto) throws InvalidParameterException;

    void delete(Long idOfferta);

    void checkFieldsValid(OffertaTempoFissoDto offertaTempoFissoDto) throws InvalidParameterException;

    void convertRelations(OffertaTempoFissoDto offertaTempoFissoDto, OffertaTempoFisso offertaTempoFisso) throws InvalidParameterException;

    void updatePresentFields(OffertaTempoFissoDto updatedOffertaTempoFissoDto, OffertaTempoFisso existingOffertaTempoFisso) throws InvalidParameterException;
}
