package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AstaInversaService {

    AstaInversaDto create(AstaInversaDto nuovaAstaInversaDto) throws InvalidParameterException;

    Page<AstaInversaDto> findAll(Pageable pageable);

    Page<AstaInversaDto> findByProprietarioIdAccountIs(Long idAccount, Pageable pageable);

    Page<AstaInversaDto> findByNomeLikeAndCategoriaNomeIs(String nomeAsta, String nomeCategoria, Pageable pageable);

    Page<AstaInversaDto> findByOfferenteIdAccountIs(Long idAccount, Pageable pageable);

    Optional<AstaInversaDto> findOne(Long idAsta);

    boolean isExists(Long idAsta);

    AstaInversaDto fullUpdate(Long idAsta, AstaInversaDto updatedAstaInversaDto) throws InvalidParameterException;

    AstaInversaDto partialUpdate(Long idAsta, AstaInversaDto updatedAstaInversaDto) throws InvalidParameterException;

    void delete(Long idAsta);

    void checkFieldsValid(AstaInversaDto astaInversaDto) throws InvalidParameterException;

    void convertRelations(AstaInversaDto astaInversaDto, AstaInversa astaInversa) throws InvalidParameterException;

    void updatePresentFields(AstaInversaDto updatedAstaInversaDto, AstaInversa existingAstaInversa) throws InvalidParameterException;
}
