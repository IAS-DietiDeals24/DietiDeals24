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

    Page<NotificaDto> findByDestinatario(Long idAccount, Pageable pageable);

    Optional<NotificaDto> findOne(Long idNotifica);

    boolean isExists(Long idNotifica);

    NotificaDto fullUpdate(Long idNotifica, NotificaDto updatedNotificaDto) throws InvalidParameterException;

    NotificaDto partialUpdate(Long idNotifica, NotificaDto updatedNotificaDto) throws InvalidParameterException;

    void delete(Long idNotifica);

    void checkFieldsValid(NotificaDto notificaDto) throws InvalidParameterException;

    void convertRelations(NotificaDto notificaDto, Notifica notifica) throws InvalidParameterException;

    void updatePresentFields(NotificaDto updatedNotificaDto, Notifica existingNotifica) throws InvalidParameterException;
}
