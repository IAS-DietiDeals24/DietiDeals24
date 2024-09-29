package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaSilenziosaDto;
import com.iasdietideals24.backend.services.OffertaSilenziosaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class OffertaSilenziosaController {

    private final OffertaSilenziosaService offertaSilenziosaService;

    public OffertaSilenziosaController(OffertaSilenziosaService offertaSilenziosaService) {
        this.offertaSilenziosaService = offertaSilenziosaService;
    }

    @PostMapping(path = "/offerte/di-compratori/silenziose")
    public ResponseEntity<OffertaSilenziosaDto> createOffertaSilenziosa(@RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {
        OffertaSilenziosaDto createdOffertaSilenziosaDto = offertaSilenziosaService.create(receivedOffertaSilenziosaDto);
        return new ResponseEntity<>(createdOffertaSilenziosaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/offerte/di-compratori/silenziose")
    public ResponseEntity<Page<OffertaSilenziosaDto>> listOfferteSilenziose(Pageable pageable) {
        Page<OffertaSilenziosaDto> foundOfferteSilenzioseDto = offertaSilenziosaService.findAll(pageable);
        return new ResponseEntity<>(foundOfferteSilenzioseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> getOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta) {
        Optional<OffertaSilenziosaDto> foundOffertaSilenziosaDto = offertaSilenziosaService.findOne(idOfferta);
        if (foundOffertaSilenziosaDto.isPresent()) {
            return new ResponseEntity<>(foundOffertaSilenziosaDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> fullUpdateOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {
        if (offertaSilenziosaService.isExists(idOfferta)) {
            OffertaSilenziosaDto updatedOffertaSilenziosaDto = offertaSilenziosaService.fullUpdate(idOfferta, receivedOffertaSilenziosaDto);
            return new ResponseEntity<>(updatedOffertaSilenziosaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> partialUpdateOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaSilenziosaDto receivedOffertaSilenziosaDto) throws InvalidParameterException {
        if (offertaSilenziosaService.isExists(idOfferta)) {
            OffertaSilenziosaDto updatedOffertaSilenziosaDto = offertaSilenziosaService.partialUpdate(idOfferta, receivedOffertaSilenziosaDto);
            return new ResponseEntity<>(updatedOffertaSilenziosaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/offerte/di-compratori/silenziose/{idOfferta}")
    public ResponseEntity<OffertaSilenziosaDto> deleteOffertaSilenziosa(@PathVariable("idOfferta") Long idOfferta) {
        offertaSilenziosaService.delete(idOfferta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
