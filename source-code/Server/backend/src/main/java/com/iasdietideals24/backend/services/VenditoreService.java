package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VenditoreService {

    VenditoreDto create(VenditoreDto nuovoVenditoreDto) throws InvalidParameterException;

    Page<VenditoreDto> findAll(Pageable pageable);

    Optional<VenditoreDto> findOne(Long idAccount);

    Page<VenditoreDto> findByTokensIdFacebook(String token, Pageable pageable);

    Page<VenditoreDto> findByEmail(String email, Pageable pageable);

    Page<VenditoreDto> findByEmailAndPassword(String email, String password, Pageable pageable);

    boolean isExists(Long idAccount);

    VenditoreDto fullUpdate(Long idAccount, VenditoreDto updatedVenditoreDto) throws InvalidParameterException;

    VenditoreDto partialUpdate(Long idAccount, VenditoreDto updatedVenditoreDto) throws InvalidParameterException;

    void delete(Long idAccount) throws InvalidParameterException;

    void checkFieldsValid(VenditoreDto venditoreDto) throws InvalidParameterException;

    void convertRelations(VenditoreDto venditoreDto, Venditore venditore) throws InvalidParameterException;

    void updatePresentFields(VenditoreDto updatedVenditoreDto, Venditore existingVenditore) throws InvalidParameterException;
}