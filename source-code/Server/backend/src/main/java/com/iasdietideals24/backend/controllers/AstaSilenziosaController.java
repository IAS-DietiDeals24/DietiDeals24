package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaSilenziosaDto;
import com.iasdietideals24.backend.services.AstaSilenziosaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AstaSilenziosaController {

    private final AstaSilenziosaService astaSilenziosaService;

    public AstaSilenziosaController(AstaSilenziosaService astaSilenziosaService) {
        this.astaSilenziosaService = astaSilenziosaService;
    }

    @PostMapping(path = "/aste/di-venditori/silenziose")
    public ResponseEntity<AstaSilenziosaDto> createAstaSilenziosa(@RequestBody AstaSilenziosaDto receivedAstaSilenziosaDto) throws InvalidParameterException {
        AstaSilenziosaDto createdAstaSilenziosaDto = astaSilenziosaService.create(receivedAstaSilenziosaDto);
        return new ResponseEntity<>(createdAstaSilenziosaDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/aste/di-venditori/silenziose")
    public ResponseEntity<Page<AstaSilenziosaDto>> listAsteSilenziose(Pageable pageable) {
        Page<AstaSilenziosaDto> foundAsteSilenzioseDto = astaSilenziosaService.findAll(pageable);
        return new ResponseEntity<>(foundAsteSilenzioseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> getAstaSilenziosa(@PathVariable("idAsta") Long idAsta) {
        Optional<AstaSilenziosaDto> foundAstaSilenziosaDto = astaSilenziosaService.findOne(idAsta);
        if (foundAstaSilenziosaDto.isPresent()) {
            return new ResponseEntity<>(foundAstaSilenziosaDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> fullUpdateAstaSilenziosa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaSilenziosaDto receivedAstaSilenziosaDto) throws InvalidParameterException {
        if (astaSilenziosaService.isExists(idAsta)) {
            AstaSilenziosaDto updatedAstaSilenziosaDto = astaSilenziosaService.fullUpdate(idAsta, receivedAstaSilenziosaDto);
            return new ResponseEntity<>(updatedAstaSilenziosaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity<AstaSilenziosaDto> partialUpdateAstaSilenziosa(@PathVariable("idAsta") Long idAsta, @RequestBody AstaSilenziosaDto receivedAstaSilenziosaDto) throws InvalidParameterException {
        if (astaSilenziosaService.isExists(idAsta)) {
            AstaSilenziosaDto updatedAstaSilenziosaDto = astaSilenziosaService.partialUpdate(idAsta, receivedAstaSilenziosaDto);
            return new ResponseEntity<>(updatedAstaSilenziosaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/aste/di-venditori/silenziose/{idAsta}")
    public ResponseEntity deleteAstaSilenziosa(@PathVariable("idAsta") Long idAsta) {
        astaSilenziosaService.delete(idAsta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}