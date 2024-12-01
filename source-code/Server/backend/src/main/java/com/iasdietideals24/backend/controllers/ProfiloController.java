package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.services.ProfiloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class ProfiloController {

    public static final String LOG_PROFILO_NON_TROVATO = "Profilo non trovato.";

    private final ProfiloService profiloService;

    public ProfiloController(ProfiloService profiloService) {
        this.profiloService = profiloService;
    }

    @PutMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> createOrFullUpdateProfilo(@PathVariable("nomeUtente") String nomeUtente, @RequestBody PutProfiloDto receivedProfiloDto) throws InvalidParameterException {

        log.info("Creazione o sostituzione del profilo in corso...");

        log.trace("Nome utente profilo da creare o sostituire: {}", nomeUtente);

        if (profiloService.isExists(nomeUtente)) {
            ProfiloDto updatedProfiloDto = profiloService.fullUpdate(nomeUtente, receivedProfiloDto);

            log.info("Profilo sostituito. Invio in corso...");

            return new ResponseEntity<>(updatedProfiloDto, HttpStatus.OK);
        } else {
            ProfiloDto createdProfiloDto = profiloService.create(nomeUtente, receivedProfiloDto);

            log.info("Profilo creato. Invio in corso...");

            return new ResponseEntity<>(createdProfiloDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/profili")
    public ResponseEntity<Page<ProfiloDto>> listProfili(Pageable pageable) {

        log.info("Recupero dei profili in corso...");

        Page<ProfiloDto> foundProfiliDto = profiloService.findAll(pageable);

        log.info("Profili recuperati. Invio in corso...");

        return new ResponseEntity<>(foundProfiliDto, HttpStatus.OK);
    }

    @GetMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> getProfilo(@PathVariable("nomeUtente") String nomeUtente) {

        log.info("Recupero del profilo in corso...");

        log.trace("Nome utente profilo da recuperare: {}", nomeUtente);

        Optional<ProfiloDto> foundProfiloDto = profiloService.findOne(nomeUtente);

        if (foundProfiloDto.isPresent()) {

            log.info("Profilo recuperato. Invio in corso...");

            return new ResponseEntity<>(foundProfiloDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_PROFILO_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> partialUpdateProfilo(@PathVariable("nomeUtente") String nomeUtente, @RequestBody ProfiloDto receivedProfiloDto) throws InvalidParameterException {

        log.info("Aggiornamento del profilo in corso...");

        log.trace("Nome utente profilo da aggiornare: {}", nomeUtente);

        if (profiloService.isExists(nomeUtente)) {
            ProfiloDto updatedProfiloDto = profiloService.partialUpdate(nomeUtente, receivedProfiloDto);

            log.info("Profilo aggiornato. Invio in corso...");

            return new ResponseEntity<>(updatedProfiloDto, HttpStatus.OK);
        } else {

            log.info(LOG_PROFILO_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> deleteProfilo(@PathVariable("nomeUtente") String nomeUtente) {

        log.info("Eliminazione del profilo in corso...");

        log.trace("Nome utente profilo da eliminare: {}", nomeUtente);

        profiloService.delete(nomeUtente);

        log.info("Profilo eliminato. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}