package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;
import com.iasdietideals24.backend.services.ProfiloService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProfiloController {

    private final ProfiloService profiloService;

    public ProfiloController(ProfiloService profiloService) {
        this.profiloService = profiloService;
    }

    @PutMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> createOrUpdateProfilo(@PathVariable("nomeUtente") String nomeUtente, @RequestBody PutProfiloDto receivedProfiloDto) {
        try {
            if (profiloService.isExists(nomeUtente)) {
                ProfiloDto createdProfiloDto = profiloService.fullUpdate(nomeUtente, receivedProfiloDto);
                return new ResponseEntity<>(createdProfiloDto, HttpStatus.OK);
            } else {
                ProfiloDto updatedProfiloDto = profiloService.create(nomeUtente, receivedProfiloDto);
                return new ResponseEntity<>(updatedProfiloDto, HttpStatus.CREATED);
            }
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/profili")
    public ResponseEntity<Page<ProfiloDto>> listProfili(Pageable pageable) {
        Page<ProfiloDto> foundProfiliDto = profiloService.findAll(pageable);
        return new ResponseEntity<>(foundProfiliDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity deleteProfilo(@PathVariable("nomeUtente") String nomeUtente) {
        profiloService.delete(nomeUtente);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}