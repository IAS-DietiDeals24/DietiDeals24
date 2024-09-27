package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaTempoFissoDto;
import com.iasdietideals24.backend.services.AstaTempoFissoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AstaTempoFissoController {

    private final AstaTempoFissoService astaTempoFissoService;

    public AstaTempoFissoController(AstaTempoFissoService astaTempoFissoService) {
        this.astaTempoFissoService = astaTempoFissoService;
    }

    @PostMapping(path = "/aste/di-venditori/tempo-fisso")
    public ResponseEntity<AstaTempoFissoDto> createAstaTempoFisso(@RequestBody AstaTempoFissoDto receivedAstaTempoFissoDto) throws InvalidParameterException {
        AstaTempoFissoDto createdAstaTempoFissoDto = astaTempoFissoService.create(receivedAstaTempoFissoDto);
        return new ResponseEntity<>(createdAstaTempoFissoDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/aste/di-venditori/tempo-fisso")
    public ResponseEntity<Page<AstaTempoFissoDto>> listAsteTempoFisso(Pageable pageable) {
        Page<AstaTempoFissoDto> foundAsteTempoFissoDto = astaTempoFissoService.findAll(pageable);
        return new ResponseEntity<>(foundAsteTempoFissoDto, HttpStatus.OK);
    }

    @GetMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> getAstaTempoFisso(@PathVariable("idAsta") Long idAsta) {
        Optional<AstaTempoFissoDto> foundAstaTempoFissoDto = astaTempoFissoService.findOne(idAsta);
        if (foundAstaTempoFissoDto.isPresent()) {
            return new ResponseEntity<>(foundAstaTempoFissoDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> fullUpdateAstaTempoFisso(@PathVariable("idAsta") Long idAsta, @RequestBody AstaTempoFissoDto receivedAstaTempoFissoDto) throws InvalidParameterException {
        if (astaTempoFissoService.isExists(idAsta)) {
            AstaTempoFissoDto updatedAstaTempoFissoDto = astaTempoFissoService.fullUpdate(idAsta, receivedAstaTempoFissoDto);
            return new ResponseEntity<>(updatedAstaTempoFissoDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity<AstaTempoFissoDto> partialUpdateAstaTempoFisso(@PathVariable("idAsta")  Long idAsta, @RequestBody AstaTempoFissoDto receivedAstaTempoFissoDto) throws InvalidParameterException {
        if (astaTempoFissoService.isExists(idAsta)) {
            AstaTempoFissoDto updatedAstaTempoFissoDto = astaTempoFissoService.partialUpdate(idAsta, receivedAstaTempoFissoDto);
            return new ResponseEntity<>(updatedAstaTempoFissoDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/aste/di-venditori/tempo-fisso/{idAsta}")
    public ResponseEntity deleteAstaTempoFisso(@PathVariable("idAsta") Long idAsta) {
        astaTempoFissoService.delete(idAsta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
