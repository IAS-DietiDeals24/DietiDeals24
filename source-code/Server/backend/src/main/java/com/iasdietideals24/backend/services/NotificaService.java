package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificaService {

    NotificaDto create(NotificaDto nuovaNotificaDto) throws InvalidParameterException;

    Page<NotificaDto> findAll(Pageable pageable);

    Optional<NotificaDto> findOne(Long idNotifica);

    boolean isExists(Long idNotifica);

    NotificaDto fullUpdate(Long idNotifica, NotificaDto updatedNotificaDto);

    NotificaDto partialUpdate(Long idNotifica, NotificaDto updatedNotificaDto);

    void delete(Long idNotifica) throws InvalidParameterException;

    void checkFieldsValid(NotificaDto notificaDto) throws InvalidParameterException;

    void convertRelations(NotificaDto notificaDto, Notifica notifica) throws InvalidParameterException;

    void updatePresentFields(NotificaDto updatedNotificaDto, Notifica existingNotifica) throws InvalidParameterException;
}
