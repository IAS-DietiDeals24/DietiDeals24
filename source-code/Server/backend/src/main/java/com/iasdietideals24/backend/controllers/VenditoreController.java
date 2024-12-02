package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import com.iasdietideals24.backend.services.VenditoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class VenditoreController {

    public static final String LOG_ACCOUNT_VENDITORE_NON_TROVATO = "Account venditore non trovato.";

    private final VenditoreService venditoreService;

    public VenditoreController(VenditoreService venditoreService) {
        this.venditoreService = venditoreService;
    }

    @PostMapping(path = "/accounts/venditori")
    public ResponseEntity<VenditoreDto> createVenditore(@RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {

        log.info("Creazione account venditore in corso...");

        VenditoreDto createdVenditoreDto = venditoreService.create(receivedVenditoreDto);

        log.info("Account venditore creato. Invio in corso...");

        return new ResponseEntity<>(createdVenditoreDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/accounts/venditori")
    public ResponseEntity<Page<VenditoreDto>> listVenditori(Pageable pageable) {

        log.info("Recupero degli account venditori in corso...");

        Page<VenditoreDto> foundVenditoriDto = venditoreService.findAll(pageable);

        log.info("Account venditori recuperati. Invio in corso...");

        return new ResponseEntity<>(foundVenditoriDto, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/venditori", params = "email")
    public ResponseEntity<Page<VenditoreDto>> listVenditoriByEmail(@RequestParam("email") String email, Pageable pageable) {

        log.info("Recupero degli account venditori in corso...");

        Page<VenditoreDto> foundVenditoriDto = venditoreService.findByEmail(email, pageable);

        log.info("Account venditori recuperati. Invio in corso...");

        return new ResponseEntity<>(foundVenditoriDto, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> getVenditore(@PathVariable("idAccount") Long idAccount) {

        log.info("Recupero dell'account venditore in corso...");

        log.trace("Id account da recuperare: {}", idAccount);

        Optional<VenditoreDto> foundVenditoreDto = venditoreService.findOne(idAccount);
        if (foundVenditoreDto.isPresent()) {

            log.info("Account venditore recuperato. Invio in corso...");

            return new ResponseEntity<>(foundVenditoreDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_ACCOUNT_VENDITORE_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> fullUpdateVenditore(@PathVariable("idAccount") Long idAccount, @RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {

        log.info("Sostituzione dell'account venditore in corso...");

        log.trace("Id account da sostituire: {}", idAccount);

        if (venditoreService.isExists(idAccount)) {
            VenditoreDto updatedVenditoreDto = venditoreService.fullUpdate(idAccount, receivedVenditoreDto);

            log.info("Account venditore sostituito. Invio in corso...");

            return new ResponseEntity<>(updatedVenditoreDto, HttpStatus.OK);
        } else {

            log.info(LOG_ACCOUNT_VENDITORE_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> partialUpdateVenditore(@PathVariable("idAccount") Long idAccount, @RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'account venditore in corso...");

        log.trace("Id account da aggiornare: {}", idAccount);

        if (venditoreService.isExists(idAccount)) {
            VenditoreDto updatedVenditoreDto = venditoreService.partialUpdate(idAccount, receivedVenditoreDto);

            log.info("Account venditore aggiornato. Invio in corso...");

            return new ResponseEntity<>(updatedVenditoreDto, HttpStatus.OK);
        } else {

            log.info(LOG_ACCOUNT_VENDITORE_NON_TROVATO);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> deleteVenditore(@PathVariable("idAccount") Long idAccount) throws InvalidParameterException {

        log.info("Eliminazione dell'account venditore in corso...");

        log.trace("Id account da eliminare: {}", idAccount);

        venditoreService.delete(idAccount);

        log.info("Account venditore eliminato. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}