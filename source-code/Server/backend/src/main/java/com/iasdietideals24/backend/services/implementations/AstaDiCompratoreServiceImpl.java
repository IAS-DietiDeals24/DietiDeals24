package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiCompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.AstaDiCompratoreService;
import com.iasdietideals24.backend.services.AstaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Service;

@Service
public class AstaDiCompratoreServiceImpl implements AstaDiCompratoreService {

    private final AstaService astaService;
    private final RelationsConverter relationsConverter;

    protected AstaDiCompratoreServiceImpl(AstaService astaService, RelationsConverter relationsConverter) {
        this.astaService = astaService;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AstaDiCompratoreDto astaDiCompratoreDto) throws InvalidParameterException {
        astaService.checkFieldsValid(astaDiCompratoreDto);
        checkProprietarioValid(astaDiCompratoreDto.getProprietarioShallow());
    }

    private void checkProprietarioValid(AccountShallowDto proprietarioShallow) {
        if (proprietarioShallow == null) {
            throw new UpdateRuntimeException("Il proprietario non può essere null!");
        }
    }

    @Override
    public void convertRelations(AstaDiCompratoreDto astaDiCompratoreDto, AstaDiCompratore astaDiCompratore) throws InvalidParameterException {
        astaService.convertRelations(astaDiCompratoreDto, astaDiCompratore);
        convertProprietarioShallow(astaDiCompratoreDto.getProprietarioShallow(), astaDiCompratore);
    }

    private void convertProprietarioShallow(AccountShallowDto proprietarioShallow, AstaDiCompratore astaDiCompratore) throws IdNotFoundException, InvalidTypeException {

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(proprietarioShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Compratore convertedCompratore) {
                astaDiCompratore.setProprietario(convertedCompratore);
                convertedCompratore.addAstaPosseduta(astaDiCompratore);
            } else {
                throw new InvalidTypeException("Un compratore può possedere solo aste di compratori!");
            }
        }
    }

    @Override
    public void updatePresentFields(AstaDiCompratoreDto updatedAstaDiCompratoreDto, AstaDiCompratore existingAstaDiCompratore) throws InvalidParameterException {
        astaService.updatePresentFields(updatedAstaDiCompratoreDto, existingAstaDiCompratore);

        // Non è possibile modificare l'associazione "proprietario" tramite la risorsa "aste/di-compratori"
    }
}
