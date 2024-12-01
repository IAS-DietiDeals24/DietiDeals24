package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import com.iasdietideals24.backend.services.OffertaSilenziosaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class OffertaSilenziosaController {

    private final OffertaSilenziosaService offertaSilenziosaService;

    public OffertaSilenziosaController(OffertaSilenziosaService offertaSilenziosaService) {
        this.offertaSilenziosaService = offertaSilenziosaService;
    }

    @PostMapping(path = "/offerte/di-compratori/silenziose")
    public ResponseEntity<OffertaSilenziosaDto> createOffertaSilenziosa(@RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {

        log.info("Creazione dell'offerta silenziosa in corso...");

        OffertaSilenziosaDto createdOffertaSilenziosaDto = offertaSilenziosaService.create(receivedOffertaSilenziosaDto);

        log.info("Offerta silenziosa creata. Invio in corso...");

        return new ResponseEntity<>(createdOffertaSilenziosaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/offerte/di-compratori/silenziose")
    public ResponseEntity<Page<OffertaSilenziosaDto>> listOfferteSilenziose(Pageable pageable) {

        log.info("Recupero delle offerte silenziose in corso...");

        Page<OffertaSilenziosaDto> foundOfferteSilenzioseDto = offertaSilenziosaService.findAll(pageable);

        log.info("Offerte silenziose recuperate. Invio in corso...");

        return new ResponseEntity<>(foundOfferteSilenzioseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> getOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Recupero dell'offerta silenziosa in corso...");

        log.trace("CONTROLLER: Id offerta da recuperare: {}", idOfferta);

        Optional<OffertaSilenziosaDto> foundOffertaSilenziosaDto = offertaSilenziosaService.findOne(idOfferta);
        if (foundOffertaSilenziosaDto.isPresent()) {

            log.info("Offerta silenziosa recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaSilenziosaDto.get(), HttpStatus.OK);
        } else {

            log.info("Offerta silenziosa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> fullUpdateOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {

        log.info("Sostituzione dell'offerta silenziosa in corso...");

        log.trace("CONTROLLER: Id offerta da sostituire: {}", idOfferta);

        if (offertaSilenziosaService.isExists(idOfferta)) {
            OffertaSilenziosaDto updatedOffertaSilenziosaDto = offertaSilenziosaService.fullUpdate(idOfferta, receivedOffertaSilenziosaDto);

            log.info("Offerta silenziosa sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaSilenziosaDto, HttpStatus.OK);
        } else {

            log.info("Offerta silenziosa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> partialUpdateOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'offerta silenziosa in corso...");

        log.trace("CONTROLLER: Id offerta da sostituire: {}", idOfferta);

        if (offertaSilenziosaService.isExists(idOfferta)) {
            OffertaSilenziosaDto updatedOffertaSilenziosaDto = offertaSilenziosaService.partialUpdate(idOfferta, receivedOffertaSilenziosaDto);

            log.info("Offerta silenziosa aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaSilenziosaDto, HttpStatus.OK);
        } else {

            log.info("Offerta silenziosa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> deleteOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Eliminazione dell'offerta silenziosa in corso...");

        log.trace("CONTROLLER: Id offerta da sostituire: {}", idOfferta);

        offertaSilenziosaService.delete(idOfferta);

        log.info("Offerta silenziosa eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}