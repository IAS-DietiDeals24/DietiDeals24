package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AstaTempoFissoService {

    AstaTempoFissoDto create(AstaTempoFissoDto nuovaAstaTempoFissoDto) throws InvalidParameterException;

    Page<AstaTempoFissoDto> findAll(Pageable pageable);

    Page<AstaTempoFissoDto> findByProprietarioIdAccountIs(Long idAccount, Pageable pageable);

    Page<AstaTempoFissoDto> findByProprietarioIdAccountNot(Long idAccount, Pageable pageable);

    Page<AstaTempoFissoDto> findByNomeLikeAndCategoriaNomeIs(String nomeAsta, String nomeCategoria, Pageable pageable);

    Page<AstaTempoFissoDto> findByOfferenteIdAccountIs(Long idAccount, Pageable pageable);

    Optional<AstaTempoFissoDto> findOne(Long idAsta);

    boolean isExists(Long idAsta);

    AstaTempoFissoDto fullUpdate(Long idAsta, AstaTempoFissoDto updatedAstaTempoFissoDto) throws InvalidParameterException;

    AstaTempoFissoDto partialUpdate(Long idAsta, AstaTempoFissoDto updatedAstaTempoFissoDto) throws InvalidParameterException;

    void delete(Long idAsta);

    void checkFieldsValid(AstaTempoFissoDto astaTempoFissoDto) throws InvalidParameterException;

    void convertRelations(AstaTempoFissoDto astaTempoFissoDto, AstaTempoFisso astaTempoFisso) throws InvalidParameterException;

    void updatePresentFields(AstaTempoFissoDto updatedAstaTempoFissoDto, AstaTempoFisso existingAstaTempoFisso) throws InvalidParameterException;

    void closeAstaTempoFisso(AstaTempoFisso expiredAsta);
}
