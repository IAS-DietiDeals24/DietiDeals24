package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfiloService {

    ProfiloDto create(String nomeUtente, PutProfiloDto nuovoProfiloDto) throws InvalidParameterException;

    Page<ProfiloDto> findAll(Pageable pageable);

    Optional<ProfiloDto> findOne(String nomeUtente);

    boolean isExists(String nomeUtente);

    ProfiloDto fullUpdate(String nomeUtente, PutProfiloDto updatedProfiloDto) throws InvalidParameterException;

    ProfiloDto partialUpdate(String nomeUtente, ProfiloDto updatedProfiloDto) throws InvalidParameterException;

    void delete(String nomeUtente);

    void validateData(ProfiloDto nuovoProfiloDto) throws InvalidParameterException;

    void validateData(PutProfiloDto nuovoProfiloDto) throws InvalidParameterException;
}