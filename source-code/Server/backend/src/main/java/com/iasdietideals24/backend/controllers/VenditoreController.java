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

    @PostMapping(path = "/accounts/venditori")
    public ResponseEntity<VenditoreDto> createVenditore(@RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {
        VenditoreDto createdVenditoreDto = venditoreService.create(receivedVenditoreDto);
        return new ResponseEntity<>(createdVenditoreDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/accounts/venditori")
    public ResponseEntity<Page<VenditoreDto>> listVenditori(Pageable pageable) {
        Page<VenditoreDto> foundVenditoriDto = venditoreService.findAll(pageable);
        return new ResponseEntity<>(foundVenditoriDto, HttpStatus.OK);
    }

    @GetMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> getVenditore(@PathVariable("idAccount") Long idAccount) {
        Optional<VenditoreDto> foundVenditoreDto = venditoreService.findOne(idAccount);
        if (foundVenditoreDto.isPresent()) {
            return new ResponseEntity<>(foundVenditoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
    @GetMapping(path = "/accounts/venditori/facebook/{idFacebook}")
    public ResponseEntity<VenditoreDto> getVenditoreFacebook(@PathVariable("idFacebook") String idFacebook) {
        Optional<VenditoreDto> foundVenditoreDto = venditoreService.findByTokensIdFacebook(idFacebook, );
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
            foundVenditoreDto = venditoreService.findByEmailAndPassword(email, password, );
        if (foundVenditoreDto.isPresent()) {
            return new ResponseEntity<>(foundVenditoreDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
     */

    @PutMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> fullUpadateVenditore(@PathVariable("idAccount") Long idAccount, @RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {
        if (venditoreService.isExists(idAccount)) {
            VenditoreDto updatedVenditoreDto = venditoreService.fullUpdate(idAccount, receivedVenditoreDto);
            return new ResponseEntity<>(updatedVenditoreDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> partialUpdateVenditore(@PathVariable("idAccount") Long idAccount, @RequestBody VenditoreDto receivedVenditoreDto) throws InvalidParameterException {
        if (venditoreService.isExists(idAccount)) {
            VenditoreDto updatedVenditoreDto = venditoreService.partialUpdate(idAccount, receivedVenditoreDto);
            return new ResponseEntity<>(updatedVenditoreDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/accounts/venditori/{idAccount}")
    public ResponseEntity<VenditoreDto> deleteVenditore(@PathVariable("idAccount") Long idAccount) throws InvalidParameterException {
        venditoreService.delete(idAccount);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
