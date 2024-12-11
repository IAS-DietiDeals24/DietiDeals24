package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import com.iasdietideals24.backend.services.AstaInversaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class AstaInversaController {

    public static final String LOG_ASTA_NON_TROVATA = "Asta inversa non trovata.";
    public static final String LOG_RECUPERO_ASTE_IN_CORSO = "Recupero delle aste inverse in corso...";
    public static final String LOG_ASTE_RECUPERATE = "Aste inverse recuperate. Invio in corso...";

    private final AstaInversaService astaInversaService;

    public AstaInversaController(AstaInversaService astaInversaService) {
        this.astaInversaService = astaInversaService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/aste/di-compratori/inverse")
    public ResponseEntity<AstaInversaDto> createAstaInversa(@RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {

        log.info("Creazione dell'asta inversa in corso...");

        AstaInversaDto createdAstaInversaDto = astaInversaService.create(receivedAstaInversaDto);

        log.info("Asta inversa creata. Invio in corso...");

        return new ResponseEntity<>(createdAstaInversaDto, HttpStatus.CREATED);
    }

    // No authentication required
    @GetMapping(path = "/aste/di-compratori/inverse")
    public ResponseEntity<Page<AstaInversaDto>> listAsteInverse(Pageable pageable) {

        log.info(LOG_RECUPERO_ASTE_IN_CORSO);

        Page<AstaInversaDto> foundAsteInverseDto = astaInversaService.findAll(pageable);

        log.info(LOG_ASTE_RECUPERATE);

        return new ResponseEntity<>(foundAsteInverseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/aste/di-compratori/inverse", params = "proprietario")
    public ResponseEntity<Page<AstaInversaDto>> listAsteInverseByProprietarioIdAccountIs(@RequestParam("proprietario") Long idAccount, Pageable pageable) {

        log.info(LOG_RECUPERO_ASTE_IN_CORSO);

        Page<AstaInversaDto> foundAsteInverseDto = astaInversaService.findByProprietarioIdAccountIs(idAccount, pageable);

        log.info(LOG_ASTE_RECUPERATE);

        return new ResponseEntity<>(foundAsteInverseDto, HttpStatus.OK);
    }

    // No authentication required
    @GetMapping(path = "/aste/di-compratori/inverse", params = {"nome", "categoria"})
    public ResponseEntity<Page<AstaInversaDto>> listAsteInverseByNomeLikeAndCategoriaNomeLike(@RequestParam("nome") String nomeAsta, @RequestParam("categoria") String nomeCategoria, Pageable pageable) {

        log.info(LOG_RECUPERO_ASTE_IN_CORSO);

        Page<AstaInversaDto> foundAsteInverseDto = astaInversaService.findByNomeLikeAndCategoriaNomeLike("%" + nomeAsta + "%", nomeCategoria, pageable);

        log.info(LOG_ASTE_RECUPERATE);

        return new ResponseEntity<>(foundAsteInverseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/aste/di-compratori/inverse", params = "offerente")
    public ResponseEntity<Page<AstaInversaDto>> listAsteInverseByOfferenteIdAccountIs(@RequestParam("offerente") Long idAccount, Pageable pageable) {

        log.info(LOG_RECUPERO_ASTE_IN_CORSO);

        Page<AstaInversaDto> foundAsteInverseDto = astaInversaService.findByOfferenteIdAccountIs(idAccount, pageable);

        log.info(LOG_ASTE_RECUPERATE);

        return new ResponseEntity<>(foundAsteInverseDto, HttpStatus.OK);
    }

    // No authentication required
    @GetMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> getAstaInversa(@PathVariable("idAsta") Long idAsta) {

        log.info("Recupero dell'asta inversa in corso...");

        log.trace("Id asta da recuperare: {}", idAsta);

        Optional<AstaInversaDto> foundAstaInversaDto = astaInversaService.findOne(idAsta);
        if (foundAstaInversaDto.isPresent()) {

            log.info("Asta inversa recuperata. Invio in corso...");

            return new ResponseEntity<>(foundAstaInversaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_ASTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> fullUpdateAstaInversa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {

        log.info("Sostituzione dell'asta inversa in corso...");

        log.trace("Id asta da sostituire: {}", idAsta);

        if (astaInversaService.isExists(idAsta)) {
            AstaInversaDto updatedAstaInversaDto = astaInversaService.fullUpdate(idAsta, receivedAstaInversaDto);

            log.info("Asta inversa sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedAstaInversaDto, HttpStatus.OK);
        } else {

            log.info(LOG_ASTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> partialUpdateAstaInversa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'asta inversa in corso...");

        log.trace("Id asta da aggiornare: {}", idAsta);

        if (astaInversaService.isExists(idAsta)) {
            AstaInversaDto updatedAstaInversaDto = astaInversaService.partialUpdate(idAsta, receivedAstaInversaDto);

            log.info("Asta inversa aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedAstaInversaDto, HttpStatus.OK);
        } else {

            log.info(LOG_ASTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> deleteAstaInversa(@PathVariable("idAsta") Long idAsta) {

        log.info("Eliminazione dell'asta inversa in corso...");

        log.trace("Id asta da eliminare: {}", idAsta);

        astaInversaService.delete(idAsta);

        log.info("Asta inversa eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}