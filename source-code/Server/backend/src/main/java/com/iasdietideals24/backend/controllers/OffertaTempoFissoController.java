package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaTempoFissoDto;
import com.iasdietideals24.backend.services.OffertaTempoFissoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class OffertaTempoFissoController {

    private final OffertaTempoFissoService offertaTempoFissoService;

    public OffertaTempoFissoController(OffertaTempoFissoService offertaTempoFissoService) {
        this.offertaTempoFissoService = offertaTempoFissoService;
    }

    @PostMapping(path = "/offerte/di-compratori/tempo-fisso")
    public ResponseEntity<OffertaTempoFissoDto> createOffertaTempoFisso(@RequestBody OffertaTempoFissoDto receivedOffertaTempoFissoDto) throws InvalidParameterException {
        OffertaTempoFissoDto createdOffertaTempoFissoDto = offertaTempoFissoService.create(receivedOffertaTempoFissoDto);
        return new ResponseEntity<>(createdOffertaTempoFissoDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/offerte/di-compratori/tempo-fisso")
    public ResponseEntity<Page<OffertaTempoFissoDto>> listOfferteTempoFisso(Pageable pageable) {
        Page<OffertaTempoFissoDto> foundOfferteTempoFissoDto = offertaTempoFissoService.findAll(pageable);
        return new ResponseEntity<>(foundOfferteTempoFissoDto, HttpStatus.OK);
    }

    @GetMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> getOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta) {
        Optional<OffertaTempoFissoDto> foundOffertaTempoFissoDto = offertaTempoFissoService.findOne(idOfferta);
        if (foundOffertaTempoFissoDto.isPresent()) {
            return new ResponseEntity<>(foundOffertaTempoFissoDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> fullUpdateOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaTempoFissoDto receivedOffertaTempoFissoDto) throws InvalidParameterException {
        if (offertaTempoFissoService.isExists(idOfferta)) {
            OffertaTempoFissoDto updatedOffertaTempoFissoDto = offertaTempoFissoService.fullUpdate(idOfferta, receivedOffertaTempoFissoDto);
            return new ResponseEntity<>(updatedOffertaTempoFissoDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> partialUpdateOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaTempoFissoDto receivedOffertaTempoFissoDto) throws InvalidParameterException {
        if (offertaTempoFissoService.isExists(idOfferta)) {
            OffertaTempoFissoDto updatedOffertaTempoFissoDto = offertaTempoFissoService.partialUpdate(idOfferta, receivedOffertaTempoFissoDto);
            return new ResponseEntity<>(updatedOffertaTempoFissoDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/offerte/di-compratori/tempo-fisso/{idOfferta}")
    public ResponseEntity<OffertaTempoFissoDto> deleteOffertaTempoFisso(@PathVariable("idOfferta") Long idOfferta) {
        offertaTempoFissoService.delete(idOfferta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
