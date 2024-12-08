package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import com.iasdietideals24.backend.services.OffertaSilenziosaService;
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
public class OffertaSilenziosaController {

    public static final String LOG_OFFERTA_NON_TROVATA = "Offerta silenziosa non trovata.";

    private final OffertaSilenziosaService offertaSilenziosaService;

    public OffertaSilenziosaController(OffertaSilenziosaService offertaSilenziosaService) {
        this.offertaSilenziosaService = offertaSilenziosaService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/offerte/di-compratori/silenziose")
    public ResponseEntity<OffertaSilenziosaDto> createOffertaSilenziosa(@RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {

        log.info("Creazione dell'offerta silenziosa in corso...");

        OffertaSilenziosaDto createdOffertaSilenziosaDto = offertaSilenziosaService.create(receivedOffertaSilenziosaDto);

        log.info("Offerta silenziosa creata. Invio in corso...");

        return new ResponseEntity<>(createdOffertaSilenziosaDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/offerte/di-compratori/silenziose")
    public ResponseEntity<Page<OffertaSilenziosaDto>> listOfferteSilenziose(Pageable pageable) {

        log.info("Recupero delle offerte silenziose in corso...");

        Page<OffertaSilenziosaDto> foundOfferteSilenzioseDto = offertaSilenziosaService.findAll(pageable);

        log.info("Offerte silenziose recuperate. Invio in corso...");

        return new ResponseEntity<>(foundOfferteSilenzioseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/offerte/di-compratori/silenziose", params = "asta_riferimento")
    public ResponseEntity<Page<OffertaSilenziosaDto>> listOfferteSilenzioseByAstaRiferimentoIdAsta(@RequestParam("asta_riferimento") Long idAsta, Pageable pageable) {

        log.info("Recupero delle offerte silenziose associate all'asta in corso...");
        log.trace("Id asta delle offerta silenziose da recuperare: {}", idAsta);

        Page<OffertaSilenziosaDto> foundOfferteSilenzioseDto = offertaSilenziosaService.findByAstaRiferimentoIdAsta(idAsta, pageable);

        log.info("Offerte silenziose associate all'asta recuperate. Invio in corso...");

        return new ResponseEntity<>(foundOfferteSilenzioseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> getOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Recupero dell'offerta silenziosa in corso...");

        log.trace("Id offerta da recuperare: {}", idOfferta);

        Optional<OffertaSilenziosaDto> foundOffertaSilenziosaDto = offertaSilenziosaService.findOne(idOfferta);
        if (foundOffertaSilenziosaDto.isPresent()) {

            log.info("Offerta silenziosa recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaSilenziosaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // No authentication required
    @GetMapping(path = "/offerte/di-compratori/silenziose/most-value", params = "asta_riferimento")
    public ResponseEntity<OffertaSilenziosaDto> findOffertaSilenziosaMaxByValoreAndAstaRiferimentoIdAstaIs(@RequestParam("asta_riferimento") Long idAsta) {

        log.info("Recupero dell'offerta silenziosa massima in corso...");

        log.trace("Id asta dell'offerta massima da recuperare: {}", idAsta);

        Optional<OffertaSilenziosaDto> foundOffertaSilenziosaDto = offertaSilenziosaService.findMaxByValoreAndAstaRiferimentoIdAstaIs(idAsta);
        if (foundOffertaSilenziosaDto.isPresent()) {

            log.info("Offerta silenziosa massima recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaSilenziosaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/offerte/di-compratori/silenziose/most-value", params = {"asta_riferimento", "compratore_collegato"})
    public ResponseEntity<OffertaSilenziosaDto> findOffertaSilenziosaMaxByValoreAndAstaRiferimentoIdAstaIsAndCompratoreCollegatoIdAccountIs(@RequestParam("asta_riferimento") Long idAsta, @RequestParam("compratore_collegato") Long idAccount) {

        log.info("Recupero dell'offerta silenziosa massima del compratore in corso...");

        log.trace("Id asta dell'offerta massima da recuperare: {}", idAsta);
        log.trace("Id compratore dell'offerta massima da recuperare: {}", idAccount);

        Optional<OffertaSilenziosaDto> foundOffertaSilenziosaDto = offertaSilenziosaService.findMaxByValoreAndAstaRiferimentoIdAstaIsAndCompratoreCollegatoIdAccountIs(idAsta, idAccount);
        if (foundOffertaSilenziosaDto.isPresent()) {

            log.info("Offerta silenziosa massima  del compratore recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaSilenziosaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> fullUpdateOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {

        log.info("Sostituzione dell'offerta silenziosa in corso...");

        log.trace("Id offerta da sostituire: {}", idOfferta);

        if (offertaSilenziosaService.isExists(idOfferta)) {
            OffertaSilenziosaDto updatedOffertaSilenziosaDto = offertaSilenziosaService.fullUpdate(idOfferta, receivedOffertaSilenziosaDto);

            log.info("Offerta silenziosa sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaSilenziosaDto, HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> partialUpdateOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'offerta silenziosa in corso...");

        log.trace("Id offerta da aggiornare: {}", idOfferta);

        if (offertaSilenziosaService.isExists(idOfferta)) {
            OffertaSilenziosaDto updatedOffertaSilenziosaDto = offertaSilenziosaService.partialUpdate(idOfferta, receivedOffertaSilenziosaDto);

            log.info("Offerta silenziosa aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaSilenziosaDto, HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> deleteOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Eliminazione dell'offerta silenziosa in corso...");

        log.trace("Id offerta da eliminare: {}", idOfferta);

        offertaSilenziosaService.delete(idOfferta);

        log.info("Offerta silenziosa eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}