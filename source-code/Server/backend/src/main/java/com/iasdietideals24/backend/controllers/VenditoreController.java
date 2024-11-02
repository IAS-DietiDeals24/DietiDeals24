package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import com.iasdietideals24.backend.services.VenditoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class VenditoreController {

    private final VenditoreService venditoreService;

    public VenditoreController(VenditoreService venditoreService) {
        this.venditoreService = venditoreService;
    }

    @PutMapping(path = "/accounts/venditori/{email}")
    public ResponseEntity<VenditoreDto> createOrFullUpadateVenditore(@PathVariable("email") String email, @RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {
        if (venditoreService.isExists(email)) {
            VenditoreDto updatedVenditoreDto = venditoreService.fullUpdate(email, receivedVenditoreDto);
            return new ResponseEntity<>(updatedVenditoreDto, HttpStatus.OK);
        } else {
            VenditoreDto createdVenditoreDto = venditoreService.create(email, receivedVenditoreDto);
            return new ResponseEntity<>(createdVenditoreDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/accounts/venditori")
    public ResponseEntity<Page<VenditoreDto>> listVenditori(Pageable pageable) {
        Page<VenditoreDto> foundVenditoriDto = venditoreService.findAll(pageable);
        return new ResponseEntity<>(foundVenditoriDto, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/venditori/facebook/{idFacebook}")
    public ResponseEntity<VenditoreDto> getVenditoreFacebook(@PathVariable("idFacebook") String idFacebook) {
        Optional<VenditoreDto> foundVenditoreDto = venditoreService.findByIdFacebook(idFacebook);
        if (foundVenditoreDto.isPresent()) {
            return new ResponseEntity<>(foundVenditoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/accounts/venditori/{email}")
    public ResponseEntity<VenditoreDto> getVenditore(
            @PathVariable("email") String email,
            @RequestParam(name = "password", required = false, defaultValue = "") String password
    ) {
        Optional<VenditoreDto> foundVenditoreDto;
        if (password.isEmpty())
            foundVenditoreDto = venditoreService.findOne(email);
        else
            foundVenditoreDto = venditoreService.findOneWithPassword(email, password);
        if (foundVenditoreDto.isPresent()) {
            return new ResponseEntity<>(foundVenditoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/accounts/venditori/{email}")
    public ResponseEntity<VenditoreDto> partialUpdateVenditore(@PathVariable("email") String email, @RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {
        if (venditoreService.isExists(email)) {
            VenditoreDto updatedVenditoreDto = venditoreService.partialUpdate(email, receivedVenditoreDto);
            return new ResponseEntity<>(updatedVenditoreDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/accounts/venditori/{email}")
    public ResponseEntity<VenditoreDto> deleteVenditore(@PathVariable("email") String email) throws InvalidParameterException {
        venditoreService.delete(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
