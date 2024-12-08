package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import com.iasdietideals24.backend.services.CategoriaAstaService;
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
public class CategoriaAstaController {

    public static final String LOG_CATEGORIA_ASTA_NON_TROVATA = "Categoria asta non trovata.";

    private final CategoriaAstaService categoriaAstaService;

    public CategoriaAstaController(CategoriaAstaService categoriaAstaService) {
        this.categoriaAstaService = categoriaAstaService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> createOrFullUpadateCategoriaAsta(@PathVariable("nome") String nome, @RequestBody CategoriaAstaDto receivedCategoriaAstaDto) throws InvalidParameterException {

        log.info("Creazione o sostituzione della categoria asta in corso...");

        log.trace("Nome categoria asta da creare o sostituire: {}", nome);

        if (categoriaAstaService.isExists(nome)) {
            CategoriaAstaDto updatedCategoriaAstaDto = categoriaAstaService.fullUpdate(nome, receivedCategoriaAstaDto);

            log.info("Categoria asta sostituita. Invio in corso...");

            return new ResponseEntity<>(updatedCategoriaAstaDto, HttpStatus.OK);
        } else {
            CategoriaAstaDto createdCategoriaAstaDto = categoriaAstaService.create(nome, receivedCategoriaAstaDto);

            log.info("Caegoria asta creata. Invio in corso...");

            return new ResponseEntity<>(createdCategoriaAstaDto, HttpStatus.CREATED);
        }
    }

    // No authentication required
    @GetMapping(path = "/categorie-asta")
    public ResponseEntity<Page<CategoriaAstaDto>> listCategorieAsta(Pageable pageable) {

        log.info("Recupero delle categorie asta in corso...");

        Page<CategoriaAstaDto> foundCategorieAstaDto = categoriaAstaService.findAll(pageable);

        log.info("Categorie asta recuperate. Invio in corso...");

        return new ResponseEntity<>(foundCategorieAstaDto, HttpStatus.OK);
    }

    // No authentication required
    @GetMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> getCategoriaAsta(@PathVariable("nome") String nome) {

        log.info("Recupero della categoria asta in corso...");

        log.trace("Nome categoria asta da recuperare: {}", nome);

        Optional<CategoriaAstaDto> foundCategoriaAstaDto = categoriaAstaService.findOne(nome);
        if (foundCategoriaAstaDto.isPresent()) {

            log.info("Categoria asta recuperata. Invio in corso...");

            return new ResponseEntity<>(foundCategoriaAstaDto.get(), HttpStatus.OK);
        } else {

            log.info(LOG_CATEGORIA_ASTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> partialUpdateCategoriaAsta(@PathVariable("nome") String nome, @RequestBody CategoriaAstaDto receivedCategoriaAstaDto) throws InvalidParameterException {

        log.info("Aggiornamento della categoria asta in corso...");

        log.trace("Nome categoria asta da aggiornare: {}", nome);

        if (categoriaAstaService.isExists(nome)) {
            CategoriaAstaDto updatedCategoriaAstaDto = categoriaAstaService.partialUpdate(nome, receivedCategoriaAstaDto);

            log.info("Categoria asta aggiornata. Invio in corso...");

            return new ResponseEntity<>(updatedCategoriaAstaDto, HttpStatus.OK);
        } else {

            log.info(LOG_CATEGORIA_ASTA_NON_TROVATA);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/categorie-asta/{nome}")
    public ResponseEntity<CategoriaAstaDto> deleteCategoriaAsta(@PathVariable("nome") String nome) throws IllegalDeleteRequestException {

        log.info("Eliminazione della categoria asta in corso...");

        log.trace("Nome categoria asta da eliminare: {}", nome);

        categoriaAstaService.delete(nome);

        log.info("Categoria asta eliminata. Invio in corso...");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}