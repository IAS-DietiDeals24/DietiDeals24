package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import com.iasdietideals24.backend.services.AstaTempoFissoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class AstaTempoFissoController {

    public static final String LOG_ASTA_A_TEMPO_FISSO_NON_TROVATA = "Asta a tempo fisso non trovata.";

    private final AstaTempoFissoService astaTempoFissoService;

    public AstaTempoFissoController(AstaTempoFissoService astaTempoFissoService) {
        this.astaTempoFissoService = astaTempoFissoService;
    }

    @PostMapping(path = "/aste/di-venditori/tempo-fisso")
    public ResponseEntity<AstaTempoFissoDto> createAstaTempoFisso(@RequestBody AstaTempoFissoDto receivedAstaTempoFissoDto) throws InvalidParameterException {

        log.info("Creazione dell'asta a tempo fisso in corso...");

        AstaTempoFissoDto createdAstaTempoFissoDto = astaTempoFissoService.create(receivedAstaTempoFissoDto);

        log.info("Asta a tempo fisso creata. Invio in corso...");

        return new ResponseEntity<>(createdAstaTempoFissoDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/aste/di-venditori/tempo-fisso")
    public ResponseEntity<Page<AstaTempoFissoDto>> listAsteTempoFisso(Pageable pageable) {

        log.info("Recupero delle aste a tempo fisso in corso...");

        Page<AstaTempoFissoDto> foundAsteTempoFissoDto = astaTempoFissoService.findAll(pageable);

        log.info("Aste a tempo fisso recuperate. Invio in corso...");

        return new ResponseEntity<>(foundAsteTempoFissoDto, HttpStatus.OK);
    }

    @GetMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> getAstaTempoFisso(@PathVariable("idAsta") Long idAsta) {

        log.info("Recupero dell'asta a tempo fisso in corso...");

        log.trace("Id asta da recuperare: {}", idAsta);

        Optional<AstaTempoFissoDto> foundAstaTempoFissoDto = astaTempoFissoService.findOne(idAsta);
        if (foundAstaTempoFissoDto.isPresent()) {

            log.info("Asta a tempo fisso recuperata. Invio in corso...");

            return new ResponseEntity<>(foundAstaTempoFissoDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_ASTA_A_TEMPO_FISSO_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> fullUpdateAstaTempoFisso(@PathVariable("idAsta") Long idAsta, @RequestBody AstaTempoFissoDto receivedAstaTempoFissoDto) throws InvalidParameterException {

        log.info("Sostituzione dell'asta a tempo fisso in corso...");

        log.trace("Id asta da sostituire: {}", idAsta);

        if (astaTempoFissoService.isExists(idAsta)) {
            AstaTempoFissoDto updatedAstaTempoFissoDto = astaTempoFissoService.fullUpdate(idAsta, receivedAstaTempoFissoDto);

            log.info("Asta a tempo fisso sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedAstaTempoFissoDto, HttpStatus.OK);
        } else {

            log.info(LOG_ASTA_A_TEMPO_FISSO_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> partialUpdateAstaTempoFisso(@PathVariable("idAsta") Long idAsta, @RequestBody AstaTempoFissoDto receivedAstaTempoFissoDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'asta a tempo fisso in corso...");

        log.trace("Id asta da aggiornare: {}", idAsta);

        if (astaTempoFissoService.isExists(idAsta)) {
            AstaTempoFissoDto updatedAstaTempoFissoDto = astaTempoFissoService.partialUpdate(idAsta, receivedAstaTempoFissoDto);

            log.info("Asta a tempo fisso aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedAstaTempoFissoDto, HttpStatus.OK);
        } else {

            log.info(LOG_ASTA_A_TEMPO_FISSO_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> deleteAstaTempoFisso(@PathVariable("idAsta") Long idAsta) {

        log.info("Eliminazione dell'asta a tempo fisso in corso...");

        log.trace("Id asta da eliminare: {}", idAsta);

        astaTempoFissoService.delete(idAsta);

        log.info("Asta a tempo fisso eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}