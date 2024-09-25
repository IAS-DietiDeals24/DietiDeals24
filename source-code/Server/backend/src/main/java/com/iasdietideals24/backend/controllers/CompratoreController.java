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

    @PutMapping(path = "/accounts/compratori")
    public ResponseEntity<Page<CompratoreDto>> listCompratori(Pageable pageable) {
        Page<CompratoreDto> foundCompratoriDto = compratoreService.findAll(pageable);
        return new ResponseEntity<>(foundCompratoriDto, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity<CompratoreDto> getCompratore(@PathVariable("email") String email) {
        Optional<CompratoreDto> foundCompratoreDto = compratoreService.findOne(email);
        if (foundCompratoreDto.isPresent()) {
            return new ResponseEntity<>(foundCompratoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity<CompratoreDto> partialUpdateCompratore(@PathVariable("email")  String email, @RequestBody CompratoreDto receivedCompratoreDto) throws InvalidParameterException {
        if (compratoreService.isExists(email)) {
            CompratoreDto updatedCompratoreDto = compratoreService.partialUpdate(email, receivedCompratoreDto);
            return new ResponseEntity<>(updatedCompratoreDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/accounts/compratori/{email}")
    public ResponseEntity deleteCompratore(@PathVariable("email") String email) throws InvalidParameterException {
        compratoreService.delete(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
