package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import com.iasdietideals24.backend.services.CategoriaAstaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CategoriaAstaController {

    private final CategoriaAstaService categoriaAstaService;

    public CategoriaAstaController(CategoriaAstaService categoriaAstaService) {
        this.categoriaAstaService = categoriaAstaService;
    }

    @PutMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> createOrFullUpadateCategoriaAsta(@PathVariable("nome") String nome, @RequestBody CategoriaAstaDto receivedCategoriaAstaDto) throws InvalidParameterException {
        if (categoriaAstaService.isExists(nome)) {
            CategoriaAstaDto updatedCategoriaAstaDto = categoriaAstaService.fullUpdate(nome, receivedCategoriaAstaDto);
            return new ResponseEntity<>(updatedCategoriaAstaDto, HttpStatus.OK);
        } else {
            CategoriaAstaDto createdCategoriaAstaDto = categoriaAstaService.create(nome, receivedCategoriaAstaDto);
            return new ResponseEntity<>(createdCategoriaAstaDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/categorie-asta")
    public ResponseEntity<Page<CategoriaAstaDto>> listCategorieAsta(Pageable pageable) {
        Page<CategoriaAstaDto> foundCategorieAstaDto = categoriaAstaService.findAll(pageable);
        return new ResponseEntity<>(foundCategorieAstaDto, HttpStatus.OK);
    }

    @GetMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> getCategoriaAsta(@PathVariable("nome") String nome) {
        Optional<CategoriaAstaDto> foundCategoriaAstaDto = categoriaAstaService.findOne(nome);
        if (foundCategoriaAstaDto.isPresent()) {
            return new ResponseEntity<>(foundCategoriaAstaDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> partialUpdateCategoriaAsta(@PathVariable("nome") String nome, @RequestBody CategoriaAstaDto receivedCategoriaAstaDto) throws InvalidParameterException {
        if (categoriaAstaService.isExists(nome)) {
            CategoriaAstaDto updatedCategoriaAstaDto = categoriaAstaService.partialUpdate(nome, receivedCategoriaAstaDto);
            return new ResponseEntity<>(updatedCategoriaAstaDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> deleteCategoriaAsta(@PathVariable("nome") String nome) throws IllegalDeleteRequestException {
        categoriaAstaService.delete(nome);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
