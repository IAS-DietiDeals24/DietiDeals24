package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import com.iasdietideals24.backend.services.OffertaTempoFissoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class OffertaTempoFissoController {

    private final OffertaTempoFissoService offertaTempoFissoService;

    public OffertaTempoFissoController(OffertaTempoFissoService offertaTempoFissoService) {
        this.offertaTempoFissoService = offertaTempoFissoService;
    }

    @PostMapping(path = "/offerte/di-compratori/tempo-fisso")
    public ResponseEntity<OffertaTempoFissoDto> createOffertaTempoFisso(@RequestBody OffertaTempoFissoDto receivedOffertaTempoFissoDto) throws InvalidParameterException {

        log.info("Creazione dell'offerta a tempo fisso in corso...");

        OffertaTempoFissoDto createdOffertaTempoFissoDto = offertaTempoFissoService.create(receivedOffertaTempoFissoDto);

        log.info("Offerta a tempo fisso creata. Invio in corso...");

        return new ResponseEntity<>(createdOffertaTempoFissoDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/offerte/di-compratori/tempo-fisso")
    public ResponseEntity<Page<OffertaTempoFissoDto>> listOfferteTempoFisso(Pageable pageable) {

        log.info("Recupero delle offerte a tempo fisso in corso...");

        Page<OffertaTempoFissoDto> foundOfferteTempoFissoDto = offertaTempoFissoService.findAll(pageable);

        log.info("Offerte a tempo fisso recuperate. Invio in corso...");

        return new ResponseEntity<>(foundOfferteTempoFissoDto, HttpStatus.OK);
    }

    @GetMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> getOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Recupero dell'offerta a tempo fisso in corso...");

        log.trace("CONTROLLER: Id offerta da recuperare: {}", idOfferta);

        Optional<OffertaTempoFissoDto> foundOffertaTempoFissoDto = offertaTempoFissoService.findOne(idOfferta);
        if (foundOffertaTempoFissoDto.isPresent()) {

            log.info("Offerta a tempo fisso recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaTempoFissoDto.get(), HttpStatus.OK);
        } else {

            log.info("Offerta a tempo fisso non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> fullUpdateOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaTempoFissoDto receivedOffertaTempoFissoDto) throws InvalidParameterException {

        log.info("Sostituzione dell'offerta a tempo fisso in corso...");

        log.trace("CONTROLLER: Id offerta da sostituire: {}", idOfferta);

        if (offertaTempoFissoService.isExists(idOfferta)) {
            OffertaTempoFissoDto updatedOffertaTempoFissoDto = offertaTempoFissoService.fullUpdate(idOfferta, receivedOffertaTempoFissoDto);

            log.info("Offerta a tempo fisso sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaTempoFissoDto, HttpStatus.OK);
        } else {

            log.info("Offerta a tempo fisso non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> partialUpdateOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaTempoFissoDto receivedOffertaTempoFissoDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'offerta a tempo fisso in corso...");

        log.trace("CONTROLLER: Id offerta da sostituire: {}", idOfferta);

        if (offertaTempoFissoService.isExists(idOfferta)) {
            OffertaTempoFissoDto updatedOffertaTempoFissoDto = offertaTempoFissoService.partialUpdate(idOfferta, receivedOffertaTempoFissoDto);

            log.info("Offerta a tempo fisso aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaTempoFissoDto, HttpStatus.OK);
        } else {

            log.info("Offerta a tempo fisso non trovata.");

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> deleteOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Eliminazione dell'offerta a tempo fisso in corso...");

        log.trace("CONTROLLER: Id offerta da sostituire: {}", idOfferta);

        offertaTempoFissoService.delete(idOfferta);

        log.info("Offerta a tempo fisso eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}