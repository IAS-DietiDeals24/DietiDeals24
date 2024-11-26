package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.services.CompratoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CompratoreController {

    private final CompratoreService compratoreService;

    public CompratoreController(CompratoreService compratoreService) {
        this.compratoreService = compratoreService;
    }

    @PutMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity<CompratoreDto> createOrFullUpadateCompratore(@PathVariable("email") String email, @RequestBody CompratoreDto receivedCompratoreDto) throws InvalidParameterException {
        if (compratoreService.isExists(email)) {
            CompratoreDto updatedCompratoreDto = compratoreService.fullUpdate(email, receivedCompratoreDto);
            return new ResponseEntity<>(updatedCompratoreDto, HttpStatus.OK);
        } else {
            CompratoreDto createdCompratoreDto = compratoreService.create(email, receivedCompratoreDto);
            return new ResponseEntity<>(createdCompratoreDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/accounts/compratori")
    public ResponseEntity<Page<CompratoreDto>> listCompratori(Pageable pageable) {
        Page<CompratoreDto> foundCompratoriDto = compratoreService.findAll(pageable);
        return new ResponseEntity<>(foundCompratoriDto, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/compratori/facebook/{idFacebook}")
    public ResponseEntity<CompratoreDto> getCompratoreFacebook(@PathVariable("idFacebook") String idFacebook) {
        Optional<CompratoreDto> foundCompratoreDto = compratoreService.findByTokensIdFacebook(idFacebook, );
        if (foundCompratoreDto.isPresent()) {
            return new ResponseEntity<>(foundCompratoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity<CompratoreDto> getCompratore(
            @PathVariable("email") String email,
            @RequestParam(name = "password", required = false, defaultValue = "") String password
    ) {
        Optional<CompratoreDto> foundCompratoreDto;
        if (password.isEmpty())
             foundCompratoreDto = compratoreService.findOne(email);
        else
            foundCompratoreDto = compratoreService.findByEmailAndPassword(email, password, );
        if (foundCompratoreDto.isPresent()) {
            return new ResponseEntity<>(foundCompratoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity<CompratoreDto> partialUpdateCompratore(@PathVariable("email") String email, @RequestBody CompratoreDto receivedCompratoreDto) throws InvalidParameterException {
        if (compratoreService.isExists(email)) {
            CompratoreDto updatedCompratoreDto = compratoreService.partialUpdate(email, receivedCompratoreDto);
            return new ResponseEntity<>(updatedCompratoreDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity<CompratoreDto> deleteCompratore(@PathVariable("email") String email) throws InvalidParameterException {
        compratoreService.delete(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
