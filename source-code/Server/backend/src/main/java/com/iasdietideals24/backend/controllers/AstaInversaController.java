package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import com.iasdietideals24.backend.services.AstaInversaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class AstaInversaController {

    private final AstaInversaService astaInversaService;

    public AstaInversaController(AstaInversaService astaInversaService) {
        this.astaInversaService = astaInversaService;
    }

    @PostMapping(path = "/aste/di-compratori/inverse")
    public ResponseEntity<AstaInversaDto> createAstaInversa(@RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {

        log.info("Creazione dell'asta inversa in corso...");

        AstaInversaDto createdAstaInversaDto = astaInversaService.create(receivedAstaInversaDto);

        log.info("Asta inversa creata. Invio in corso...");

        return new ResponseEntity<>(createdAstaInversaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/aste/di-compratori/inverse")
    public ResponseEntity<Page<AstaInversaDto>> listAsteInverse(Pageable pageable) {

        log.info("Recupero delle aste inverse in corso...");

        Page<AstaInversaDto> foundAsteInverseDto = astaInversaService.findAll(pageable);

        log.info("Aste inverse recuperate. Invio in corso...");

        return new ResponseEntity<>(foundAsteInverseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> getAstaInversa(@PathVariable("idAsta") Long idAsta) {

        log.info("Recupero dell'asta inversa in corso...");

        log.trace("CONTROLLER: Id asta da recuperare: {}", idAsta);

        Optional<AstaInversaDto> foundAstaInversaDto = astaInversaService.findOne(idAsta);
        if (foundAstaInversaDto.isPresent()) {

            log.info("Asta inversa recuperata. Invio in corso...");

            return new ResponseEntity<>(foundAstaInversaDto.get(), HttpStatus.OK);
        } else {

            log.info("Asta inversa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> fullUpdateAstaInversa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {

        log.info("Sostituzione dell'asta inversa in corso...");

        log.trace("CONTROLLER: Id asta da sostituire: {}", idAsta);

        if (astaInversaService.isExists(idAsta)) {
            AstaInversaDto updatedAstaInversaDto = astaInversaService.fullUpdate(idAsta, receivedAstaInversaDto);

            log.info("Asta inversa sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedAstaInversaDto, HttpStatus.OK);
        } else {

            log.info("Asta inversa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> partialUpdateAstaInversa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'asta inversa in corso...");

        log.trace("CONTROLLER: Id asta da sostituire: {}", idAsta);

        if (astaInversaService.isExists(idAsta)) {
            AstaInversaDto updatedAstaInversaDto = astaInversaService.partialUpdate(idAsta, receivedAstaInversaDto);

            log.info("Asta inversa aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedAstaInversaDto, HttpStatus.OK);
        } else {

            log.info("Asta inversa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> deleteAstaInversa(@PathVariable("idAsta") Long idAsta) {

        log.info("Eliminazione dell'asta inversa in corso...");

        log.trace("CONTROLLER: Id asta da sostituire: {}", idAsta);

        astaInversaService.delete(idAsta);

        log.info("Asta inversa eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}