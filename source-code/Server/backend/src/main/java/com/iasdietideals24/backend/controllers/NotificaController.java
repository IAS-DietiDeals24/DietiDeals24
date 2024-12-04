package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import com.iasdietideals24.backend.services.NotificaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class NotificaController {

    public static final String LOG_NOTIFICA_NON_TROVATA = "Notifica non trovata.";

    private final NotificaService notificaService;

    public NotificaController(NotificaService notificaService) {
        this.notificaService = notificaService;
    }

    @PostMapping(path = "/notifiche")
    public ResponseEntity<NotificaDto> createNotifica(@RequestBody NotificaDto receivedNotificaDto) throws InvalidParameterException {

        log.info("Creazione della notifica in corso...");

        NotificaDto createdNotificaDto = notificaService.create(receivedNotificaDto);

        log.info("Notifica creata. Invio in corso...");

        return new ResponseEntity<>(createdNotificaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/notifiche")
    public ResponseEntity<Page<NotificaDto>> listNotifiche(Pageable pageable) {

        log.info("Recupero delle notifiche in corso...");

        Page<NotificaDto> foundNotificheDto = notificaService.findAll(pageable);

        log.info("Notifiche recuperate. Invio in corso...");

        return new ResponseEntity<>(foundNotificheDto, HttpStatus.OK);
    }

    @GetMapping(path = "/notifiche", params = "destinatario")
    public ResponseEntity<Page<NotificaDto>> listNotificheByDestinatariIdAccount(@RequestParam("destinatario") Long idAccount, Pageable pageable) {

        log.info("Recupero delle notifiche in corso...");

        Page<NotificaDto> foundNotificheDto = notificaService.findByDestinatariIdAccount(idAccount, pageable);

        log.info("Notifiche recuperate. Invio in corso...");

        return new ResponseEntity<>(foundNotificheDto, HttpStatus.OK);
    }

    @GetMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> getNotifica(@PathVariable("idNotifica") Long idNotifica) {

        log.info("Recupero della notifica in corso...");

        log.trace("Id notifica da recuperare: {}", idNotifica);

        Optional<NotificaDto> foundNotificaDto = notificaService.findOne(idNotifica);
        if (foundNotificaDto.isPresent()) {

            log.info("Notifica recuperata. Invio in corso...");

            return new ResponseEntity<>(foundNotificaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_NOTIFICA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> fullUpdateNotifica(@PathVariable("idNotifica") Long idNotifica, @RequestBody NotificaDto receivedNotificaDto) throws InvalidParameterException {

        log.info("Sostituzione della notifica in corso...");

        log.trace("Id notifica da sostituire: {}", idNotifica);

        if (notificaService.isExists(idNotifica)) {
            NotificaDto updatedNotificaDto = notificaService.fullUpdate(idNotifica, receivedNotificaDto);

            log.info("Notifica sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedNotificaDto, HttpStatus.OK);
        } else {

            log.info(LOG_NOTIFICA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> partialUpdateNotifica(@PathVariable("idNotifica") Long idNotifica, @RequestBody NotificaDto receivedNotificaDto) throws InvalidParameterException {

        log.info("Aggiornamento della notifica in corso...");

        log.trace("Id notifica da aggiornare: {}", idNotifica);

        if (notificaService.isExists(idNotifica)) {
            NotificaDto updatedNotificaDto = notificaService.partialUpdate(idNotifica, receivedNotificaDto);

            log.info("Notifica aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedNotificaDto, HttpStatus.OK);
        } else {

            log.info(LOG_NOTIFICA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> deleteNotifica(@PathVariable("idNotifica") Long idNotifica) {

        log.info("Eliminazione della notifica in corso...");

        log.trace("Id asta da eliminare: {}", idNotifica);

        notificaService.delete(idNotifica);

        log.info("Notifica eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}