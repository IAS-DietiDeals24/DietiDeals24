package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import com.iasdietideals24.backend.services.AstaInversaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AstaInversaController {

    private final AstaInversaService astaInversaService;

    public AstaInversaController(AstaInversaService astaInversaService) {
        this.astaInversaService = astaInversaService;
    }

    @PostMapping(path = "/aste/di-compratori/inverse")
    public ResponseEntity<AstaInversaDto> createAstaInversa(@RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {
        AstaInversaDto createdAstaInversaDto = astaInversaService.create(receivedAstaInversaDto);
        return new ResponseEntity<>(createdAstaInversaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/aste/di-compratori/inverse")
    public ResponseEntity<Page<AstaInversaDto>> listAsteInverse(Pageable pageable) {
        Page<AstaInversaDto> foundAsteInverseDto = astaInversaService.findAll(pageable);
        return new ResponseEntity<>(foundAsteInverseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> getAstaInversa(@PathVariable("idAsta") Long idAsta) {
        Optional<AstaInversaDto> foundAstaInversaDto = astaInversaService.findOne(idAsta);
        if (foundAstaInversaDto.isPresent()) {
            return new ResponseEntity<>(foundAstaInversaDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> fullUpdateAstaInversa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {
        if (astaInversaService.isExists(idAsta)) {
            AstaInversaDto updatedAstaInversaDto = astaInversaService.fullUpdate(idAsta, receivedAstaInversaDto);
            return new ResponseEntity<>(updatedAstaInversaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity<AstaInversaDto> partialUpdateAstaInversa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaInversaDto receivedAstaInversaDto) throws InvalidParameterException {
        if (astaInversaService.isExists(idAsta)) {
            AstaInversaDto updatedAstaInversaDto = astaInversaService.partialUpdate(idAsta, receivedAstaInversaDto);
            return new ResponseEntity<>(updatedAstaInversaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/aste/di-compratori/inverse/{idAsta}")
    public ResponseEntity deleteAstaInversa(@PathVariable("idAsta") Long idAsta) {
        astaInversaService.delete(idAsta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
