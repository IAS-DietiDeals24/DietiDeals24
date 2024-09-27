package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.NotificaDto;
import com.iasdietideals24.backend.services.NotificaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class NotificaController {

    private final NotificaService notificaService;

    public NotificaController(NotificaService notificaService) {
        this.notificaService = notificaService;
    }

    @PostMapping(path = "/notifiche")
    public ResponseEntity<NotificaDto> createNotifica(@RequestBody NotificaDto receivedNotificaDto) throws InvalidParameterException {
        NotificaDto createdNotificaDto = notificaService.create(receivedNotificaDto);
        return new ResponseEntity<>(createdNotificaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/notifiche")
    public ResponseEntity<Page<NotificaDto>> listNotifiche(Pageable pageable) {
        Page<NotificaDto> foundNotificheDto = notificaService.findAll(pageable);
        return new ResponseEntity<>(foundNotificheDto, HttpStatus.OK);
    }

    @GetMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> getNotifica(@PathVariable("idNotifica") Long idNotifica) {
        Optional<NotificaDto> foundNotificaDto = notificaService.findOne(idNotifica);
        if (foundNotificaDto.isPresent()) {
            return new ResponseEntity<>(foundNotificaDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> fullUpdateNotifica(@PathVariable("idNotifica") Long idNotifica, @RequestBody NotificaDto receivedNotificaDto) throws InvalidParameterException {
        if (notificaService.isExists(idNotifica)) {
            NotificaDto updatedNotificaDto = notificaService.fullUpdate(idNotifica, receivedNotificaDto);
            return new ResponseEntity<>(updatedNotificaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity<NotificaDto> partialUpdateNotifica(@PathVariable("idNotifica") Long idNotifica, @RequestBody NotificaDto receivedNotificaDto) throws InvalidParameterException {
        if (notificaService.isExists(idNotifica)) {
            NotificaDto updatedNotificaDto = notificaService.partialUpdate(idNotifica, receivedNotificaDto);
            return new ResponseEntity<>(updatedNotificaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/notifiche/{idNotifica}")
    public ResponseEntity deleteNotifica(@PathVariable("idNotifica") Long idNotifica) {
        notificaService.delete(idNotifica);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
