package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoriaAstaService {

    CategoriaAstaDto create(String nome, CategoriaAstaDto nuovoCategoriaAstaDto) throws InvalidParameterException;

    Page<CategoriaAstaDto> findAll(Pageable pageable);

    Optional<CategoriaAstaDto> findOne(String nome);

    boolean isExists(String nome);

    CategoriaAstaDto fullUpdate(String nome, CategoriaAstaDto updatedCategoriaAstaDto) throws InvalidParameterException;

    CategoriaAstaDto partialUpdate(String nome, CategoriaAstaDto updatedCategoriaAstaDto) throws InvalidParameterException;

    void delete(String nome) throws IllegalDeleteRequestException;

    void checkFieldsValid(CategoriaAstaDto categoriaAstaDto) throws InvalidParameterException;

    void convertRelations(CategoriaAstaDto categoriaAstaDto, CategoriaAsta categoriaAsta) throws InvalidParameterException;

    void updatePresentFields(CategoriaAstaDto updatedCategoriaAstaDto, CategoriaAsta existingCategoriaAsta) throws InvalidParameterException;
}
