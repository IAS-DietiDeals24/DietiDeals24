package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import com.iasdietideals24.backend.services.OffertaInversaService;
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
public class OffertaInversaController {

    public static final String LOG_OFFERTA_NON_TROVATA = "Offerta inversa non trovata.";

    private final OffertaInversaService offertaInversaService;

    public OffertaInversaController(OffertaInversaService offertaInversaService) {
        this.offertaInversaService = offertaInversaService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/offerte/di-venditori/inverse")
    public ResponseEntity<OffertaInversaDto> createOffertaInversa(@RequestBody OffertaInversaDto receivedOffertaInversaDto) throws InvalidParameterException {

        log.info("Creazione dell'offerta inversa in corso...");

        OffertaInversaDto createdOffertaInversaDto = offertaInversaService.create(receivedOffertaInversaDto);

        log.info("Offerta inversa creata. Invio in corso...");

        return new ResponseEntity<>(createdOffertaInversaDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/offerte/di-venditori/inverse")
    public ResponseEntity<Page<OffertaInversaDto>> listOfferteInverse(Pageable pageable) {

        log.info("Recupero delle offerte inverse in corso...");

        Page<OffertaInversaDto> foundOfferteInverseDto = offertaInversaService.findAll(pageable);

        log.info("Offerte inverse recuperate. Invio in corso...");

        return new ResponseEntity<>(foundOfferteInverseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/offerte/di-venditori/inverse", params = "asta_riferimento")
    public ResponseEntity<Page<OffertaInversaDto>> listOfferteInverseByAstaRiferimentoIdAsta(@RequestParam("asta_riferimento") Long idAsta, Pageable pageable) {

        log.info("Recupero delle offerte inverse associate all'asta in corso...");
        log.trace("Id asta delle offerta inverse da recuperare: {}", idAsta);

        Page<OffertaInversaDto> foundOfferteInverseDto = offertaInversaService.findByAstaRiferimentoIdAsta(idAsta, pageable);

        log.info("Offerte inverse associate all'asta recuperate. Invio in corso...");

        return new ResponseEntity<>(foundOfferteInverseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> getOffertaInversa(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Recupero dell'offerta inversa in corso...");

        log.trace("Id offerta da recuperare: {}", idOfferta);

        Optional<OffertaInversaDto> foundOffertaInversaDto = offertaInversaService.findOne(idOfferta);
        if (foundOffertaInversaDto.isPresent()) {

            log.info("Offerta inversa recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaInversaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // No authentication required
    @GetMapping(path = "/offerte/di-venditori/inverse/least-value", params = "asta_riferimento")
    public ResponseEntity<OffertaInversaDto> findOffertaInversaMinByValoreAndAstaRiferimentoIdAstaIs(@RequestParam("asta_riferimento") Long idAsta) {

        log.info("Recupero dell'offerta inversa minima in corso...");

        log.trace("Id asta dell'offerta minima da recuperare: {}", idAsta);

        Optional<OffertaInversaDto> foundOffertaInversaDto = offertaInversaService.findMinByValoreAndAstaRiferimentoIdAstaIs(idAsta);
        if (foundOffertaInversaDto.isPresent()) {

            log.info("Offerta inversa minima recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaInversaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/offerte/di-venditori/inverse/least-value", params = {"asta_riferimento", "venditore_collegato"})
    public ResponseEntity<OffertaInversaDto> findOffertaInversaMinByValoreAndAstaRiferimentoIdAstaIsAndVenditoreCollegatoIdAccountIs(@RequestParam("asta_riferimento") Long idAsta, @RequestParam("venditore_collegato") Long idAccount) {

        log.info("Recupero dell'offerta inversa minima del venditore in corso...");

        log.trace("Id asta dell'offerta minima da recuperare: {}", idAsta);
        log.trace("Id venditore dell'offerta minima da recuperare: {}", idAccount);

        Optional<OffertaInversaDto> foundOffertaInversaDto = offertaInversaService.findMinByValoreAndAstaRiferimentoIdAstaIsAndVenditoreCollegatoIdAccountIs(idAsta, idAccount);
        if (foundOffertaInversaDto.isPresent()) {

            log.info("Offerta inversa minima  del venditore recuperata. Invio in corso...");

            return new ResponseEntity<>(foundOffertaInversaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> fullUpdateOffertaInversa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaInversaDto receivedOffertaInversaDto) throws InvalidParameterException {

        log.info("Sostituzione dell'offerta inversa in corso...");

        log.trace("Id offerta da sostituire: {}", idOfferta);

        if (offertaInversaService.isExists(idOfferta)) {
            OffertaInversaDto updatedOffertaInversaDto = offertaInversaService.fullUpdate(idOfferta, receivedOffertaInversaDto);

            log.info("Offerta inversa sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaInversaDto, HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> partialUpdateOffertaInversa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaInversaDto receivedOffertaInversaDto) throws InvalidParameterException {

        log.info("Aggiornamento dell'offerta inversa in corso...");

        log.trace("Id offerta da aggiornare: {}", idOfferta);

        if (offertaInversaService.isExists(idOfferta)) {
            OffertaInversaDto updatedOffertaInversaDto = offertaInversaService.partialUpdate(idOfferta, receivedOffertaInversaDto);

            log.info("Offerta inversa aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedOffertaInversaDto, HttpStatus.OK);
        } else {

            log.info(LOG_OFFERTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> deleteOffertaInversa(@PathVariable("idOfferta") Long idOfferta) {

        log.info("Eliminazione dell'offerta inversa in corso...");

        log.trace("Id offerta da eliminare: {}", idOfferta);

        offertaInversaService.delete(idOfferta);

        log.info("Offerta inversa eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}