package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaInversaDto;
import com.iasdietideals24.backend.services.OffertaInversaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class OffertaInversaController {

    private final OffertaInversaService offertaInversaService;

    public OffertaInversaController(OffertaInversaService offertaInversaService) {
        this.offertaInversaService = offertaInversaService;
    }

    @PostMapping(path = "/offerte/di-venditori/inverse")
    public ResponseEntity<OffertaInversaDto> createOffertaInversa(@RequestBody OffertaInversaDto receivedOffertaInversaDto) throws InvalidParameterException {
        OffertaInversaDto createdOffertaInversaDto = offertaInversaService.create(receivedOffertaInversaDto);
        return new ResponseEntity<>(createdOffertaInversaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/offerte/di-venditori/inverse")
    public ResponseEntity<Page<OffertaInversaDto>> listOfferteInverse(Pageable pageable) {
        Page<OffertaInversaDto> foundOfferteInverseDto = offertaInversaService.findAll(pageable);
        return new ResponseEntity<>(foundOfferteInverseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> getOffertaInversa(@PathVariable("idOfferta") Long idOfferta) {
        Optional<OffertaInversaDto> foundOffertaInversaDto = offertaInversaService.findOne(idOfferta);
        if (foundOffertaInversaDto.isPresent()) {
            return new ResponseEntity<>(foundOffertaInversaDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> fullUpdateOffertaInversa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaInversaDto receivedOffertaInversaDto) throws InvalidParameterException {
        if (offertaInversaService.isExists(idOfferta)) {
            OffertaInversaDto updatedOffertaInversaDto = offertaInversaService.fullUpdate(idOfferta, receivedOffertaInversaDto);
            return new ResponseEntity<>(updatedOffertaInversaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> partialUpdateOffertaInversa(@PathVariable("idOfferta") Long idOfferta, @RequestBody OffertaInversaDto receivedOffertaInversaDto) throws InvalidParameterException {
        if (offertaInversaService.isExists(idOfferta)) {
            OffertaInversaDto updatedOffertaInversaDto = offertaInversaService.partialUpdate(idOfferta, receivedOffertaInversaDto);
            return new ResponseEntity<>(updatedOffertaInversaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/offerte/di-venditori/inverse/{idOfferta}")
    public ResponseEntity<OffertaInversaDto> deleteOffertaInversa(@PathVariable("idOfferta") Long idOfferta) {
        offertaInversaService.delete(idOfferta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
