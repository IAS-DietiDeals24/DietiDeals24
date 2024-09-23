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

import java.util.Optional;

@RestController
public class ProfiloController {

    private final ProfiloService profiloService;

    public ProfiloController(ProfiloService profiloService) {
        this.profiloService = profiloService;
    }

    @PutMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> createOrFullUpdateProfilo(@PathVariable("nomeUtente") String nomeUtente, @RequestBody PutProfiloDto receivedProfiloDto) throws InvalidParameterException {
        if (profiloService.isExists(nomeUtente)) {
            ProfiloDto updatedProfiloDto = profiloService.fullUpdate(nomeUtente, receivedProfiloDto);
            return new ResponseEntity<>(updatedProfiloDto, HttpStatus.OK);
        } else {
            ProfiloDto createdProfiloDto = profiloService.create(nomeUtente, receivedProfiloDto);
            return new ResponseEntity<>(createdProfiloDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/profili")
    public ResponseEntity<Page<ProfiloDto>> listProfili(Pageable pageable) {
        Page<ProfiloDto> foundProfiliDto = profiloService.findAll(pageable);
        return new ResponseEntity<>(foundProfiliDto, HttpStatus.OK);
    }

    @GetMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> getProfilo(@PathVariable("nomeUtente") String nomeUtente) {
        Optional<ProfiloDto> foundProfiloDto = profiloService.findOne(nomeUtente);
        if (foundProfiloDto.isPresent()) {
            return new ResponseEntity<>(foundProfiloDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity<ProfiloDto> partialUpdateProfilo(@PathVariable("nomeUtente")  String nomeUtente, @RequestBody ProfiloDto receivedProfiloDto) throws InvalidParameterException {
        if (profiloService.isExists(nomeUtente)) {
            ProfiloDto updatedProfiloDto = profiloService.partialUpdate(nomeUtente, receivedProfiloDto);
            return new ResponseEntity<>(updatedProfiloDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/profili/{nomeUtente}")
    public ResponseEntity deleteProfilo(@PathVariable("nomeUtente") String nomeUtente) {
        profiloService.delete(nomeUtente);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}