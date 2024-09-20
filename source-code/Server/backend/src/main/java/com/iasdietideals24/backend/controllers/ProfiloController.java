package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.mapstruct.dto.ProfiloDto;
import com.iasdietideals24.backend.mapstruct.mappers.ProfiloMapper;
import com.iasdietideals24.backend.services.ProfiloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfiloController {

    private ProfiloService profiloService;
    private ProfiloMapper profiloMapper;

    public ProfiloController(ProfiloMapper profiloMapper, ProfiloService profiloService) {
        this.profiloMapper = profiloMapper;
        this.profiloService = profiloService;
    }

    @PutMapping(path = "/profilo/{nomeUtente}")
    public ResponseEntity<ProfiloDto> createOrFullUpdateProfilo(@PathVariable String nomeUtente, @RequestBody ProfiloDto profiloDto) {

        ProfiloDto savedOrUpdatedProfilo = null;

        if (profiloService.isExists(nomeUtente)) {
            return new ResponseEntity<>(savedOrUpdatedProfilo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedOrUpdatedProfilo, HttpStatus.CREATED);
        }
    }
}
