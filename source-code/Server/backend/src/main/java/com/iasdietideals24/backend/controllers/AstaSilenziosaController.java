package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import com.iasdietideals24.backend.services.AstaSilenziosaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class AstaSilenziosaController {

    private final AstaSilenziosaService astaSilenziosaService;

    public AstaSilenziosaController(AstaSilenziosaService astaSilenziosaService) {
        this.astaSilenziosaService = astaSilenziosaService;
    }

    @PostMapping(path = "/aste/di-venditori/silenziose")
    public ResponseEntity<AstaSilenziosaDto> createAstaSilenziosa(@RequestBody AstaSilenziosaDto receivedAstaSilenziosaDto) throws InvalidParameterException {

        log.info("Creazione dell'asta silenziosa in corso...");

        AstaSilenziosaDto createdAstaSilenziosaDto = astaSilenziosaService.create(receivedAstaSilenziosaDto);

        log.info("Asta silenziosa creata. Invio in corso...");

        return new ResponseEntity<>(createdAstaSilenziosaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/aste/di-venditori/silenziose")
    public ResponseEntity<Page<AstaSilenziosaDto>> listAsteSilenziose(Pageable pageable) {

        log.info("Recupero delle aste silenziose in corso...");

        Page<AstaSilenziosaDto> foundAsteSilenzioseDto = astaSilenziosaService.findAll(pageable);

        log.info("Aste silenziose recuperate. Invio in corso...");

        return new ResponseEntity<>(foundAsteSilenzioseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> getAstaSilenziosa(@PathVariable("idAsta") Long idAsta) {

        log.info("Recupero dell'asta silenziosa in corso...");

        log.trace("CONTROLLER: Id asta da recuperare: {}", idAsta);

        Optional<AstaSilenziosaDto> foundAstaSilenziosaDto = astaSilenziosaService.findOne(idAsta);
        if (foundAstaSilenziosaDto.isPresent()) {

            log.info("Asta silenziosa recuperata. Invio in corso...");

            return new ResponseEntity<>(foundAstaSilenziosaDto.get(), HttpStatus.OK);
        } else {

            log.info("Asta silenziosa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> fullUpdateAstaSilenziosa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaSilenziosaDto receivedAstaSilenziosaDto) throws InvalidParameterException {

        log.info("Sostituzione dell'asta silenziosa in corso...");

        log.trace("CONTROLLER: Id asta da sostituire: {}", idAsta);

        if (astaSilenziosaService.isExists(idAsta)) {
            AstaSilenziosaDto updatedAstaSilenziosaDto = astaSilenziosaService.fullUpdate(idAsta, receivedAstaSilenziosaDto);

            log.info("Asta silenziosa sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedAstaSilenziosaDto, HttpStatus.OK);
        } else {

            log.info("Asta silenziosa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> partialUpdateAstaSilenziosa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaSilenziosaDto receivedAstaSilenziosaDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'asta silenziosa in corso...");

        log.trace("CONTROLLER: Id asta da sostituire: {}", idAsta);

        if (astaSilenziosaService.isExists(idAsta)) {
            AstaSilenziosaDto updatedAstaSilenziosaDto = astaSilenziosaService.partialUpdate(idAsta, receivedAstaSilenziosaDto);

            log.info("Asta silenziosa aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedAstaSilenziosaDto, HttpStatus.OK);
        } else {

            log.info("Asta silenziosa non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> deleteAstaSilenziosa(@PathVariable("idAsta") Long idAsta) {

        log.info("Eliminazione dell'asta silenziosa in corso...");

        log.trace("CONTROLLER: Id asta da sostituire: {}", idAsta);

        astaSilenziosaService.delete(idAsta);

        log.info("Asta silenziosa eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}