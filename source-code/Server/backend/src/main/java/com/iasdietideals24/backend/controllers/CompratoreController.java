package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.services.CompratoreService;
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
public class CompratoreController {

    public static final String LOG_ACCOUNT_NON_TROVATO = "Account compratore non trovato.";

    private final CompratoreService compratoreService;

    public CompratoreController(CompratoreService compratoreService) {
        this.compratoreService = compratoreService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/accounts/compratori")
    public ResponseEntity<CompratoreDto> createCompratore(@RequestBody CompratoreDto receivedCompratoreDto) throws InvalidParameterException {

        log.info("Creazione account compratore in corso...");

        CompratoreDto createdCompratoreDto = compratoreService.create(receivedCompratoreDto);

        log.info("Account compratore creato. Invio in corso...");

        return new ResponseEntity<>(createdCompratoreDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/accounts/compratori")
    public ResponseEntity<Page<CompratoreDto>> listCompratori(Pageable pageable) {

        log.info("Recupero degli account compratori in corso...");

        Page<CompratoreDto> foundCompratoriDto = compratoreService.findAll(pageable);

        log.info("Account compratori recuperati. Invio in corso...");

        return new ResponseEntity<>(foundCompratoriDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/accounts/compratori", params = "email")
    public ResponseEntity<Page<CompratoreDto>> listCompratoriByEmailIs(@RequestParam("email") String email, Pageable pageable) {

        log.info("Recupero degli account compratori in corso...");

        Page<CompratoreDto> foundCompratoriDto = compratoreService.findByEmailIs(email, pageable);

        log.info("Account compratori recuperati. Invio in corso...");

        return new ResponseEntity<>(foundCompratoriDto, HttpStatus.OK);
    }

    // No authentication required
    @GetMapping(path = "/accounts/compratori/{idAccount}")
    public ResponseEntity<CompratoreDto> getCompratore(@PathVariable("idAccount") Long idAccount) {

        log.info("Recupero dell'account compratore in corso...");

        log.trace("Id account da recuperare: {}", idAccount);

        Optional<CompratoreDto> foundCompratoreDto = compratoreService.findOne(idAccount);
        if (foundCompratoreDto.isPresent()) {

            log.info("Account compratore recuperato. Invio in corso...");

            return new ResponseEntity<>(foundCompratoreDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_ACCOUNT_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/accounts/compratori/{idAccount}")
    public ResponseEntity<CompratoreDto> fullUpdateCompratore(@PathVariable("idAccount") Long idAccount, @RequestBody CompratoreDto receivedCompratoreDto) throws InvalidParameterException {

        log.info("Sostituzione dell'account compratore in corso...");

        log.trace("Id account da sostituire: {}", idAccount);

        if (compratoreService.isExists(idAccount)) {
            CompratoreDto updatedCompratoreDto = compratoreService.fullUpdate(idAccount, receivedCompratoreDto);

            log.info("Account compratore sostituito. Invio in corso...");

            return new ResponseEntity<>(updatedCompratoreDto, HttpStatus.OK);
        } else {

            log.info(LOG_ACCOUNT_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/accounts/compratori/{idAccount}")
    public ResponseEntity<CompratoreDto> partialUpdateCompratore(@PathVariable("idAccount") Long idAccount, @RequestBody CompratoreDto receivedCompratoreDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'account compratore in corso...");

        log.trace("Id account da aggiornare: {}", idAccount);

        if (compratoreService.isExists(idAccount)) {
            CompratoreDto updatedCompratoreDto = compratoreService.partialUpdate(idAccount, receivedCompratoreDto);

            log.info("Account compratore aggiornato. Invio in corso...");

            return new ResponseEntity<>(updatedCompratoreDto, HttpStatus.OK);
        } else {

            log.info(LOG_ACCOUNT_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/accounts/compratori/{idAccount}")
    public ResponseEntity<CompratoreDto> deleteCompratore(@PathVariable("idAccount") Long idAccount) throws InvalidParameterException {

        log.info("Eliminazione dell'account compratore in corso...");

        log.trace("Id account da eliminare: {}", idAccount);

        compratoreService.delete(idAccount);

        log.info("Account compratore eliminato. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}