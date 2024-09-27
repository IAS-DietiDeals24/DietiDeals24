package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.AstaDiVenditore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiVenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.AstaDiVenditoreService;
import com.iasdietideals24.backend.services.AstaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Service;

@Service
public class AstaDiVenditoreServiceImpl implements AstaDiVenditoreService {

    private final AstaService astaService;
    private final RelationsConverter relationsConverter;

    protected AstaDiVenditoreServiceImpl(AstaService astaService, RelationsConverter relationsConverter) {
        this.astaService = astaService;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AstaDiVenditoreDto astaDiVenditoreDto) throws InvalidParameterException {
        astaService.checkFieldsValid(astaDiVenditoreDto);
        checkProprietarioValid(astaDiVenditoreDto.getProprietarioShallow());
    }

    private void checkProprietarioValid(AccountShallowDto proprietarioShallow) {
        if (proprietarioShallow == null)  {
            throw new UpdateRuntimeException("Il proprietario non può essere null!");
        }
    }

    @Override
    public void convertRelations(AstaDiVenditoreDto astaDiVenditoreDto, AstaDiVenditore astaDiVenditore) throws InvalidParameterException {
        astaService.convertRelations(astaDiVenditoreDto, astaDiVenditore);
        convertProprietarioShallow(astaDiVenditoreDto.getProprietarioShallow(), astaDiVenditore);
    }

    private void convertProprietarioShallow(AccountShallowDto proprietarioShallow, AstaDiVenditore astaDiVenditore) throws IdNotFoundException, InvalidTypeException {

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(proprietarioShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Venditore convertedVenditore) {
                astaDiVenditore.setProprietario(convertedVenditore);
                convertedVenditore.addAstaPosseduta(astaDiVenditore);
            } else {
                throw new InvalidTypeException("Un venditore può possedere solo aste di venditori!");
            }
        }
    }

    @Override
    public void updatePresentFields(AstaDiVenditoreDto updatedAstaDiVenditoreDto, AstaDiVenditore existingAstaDiVenditore) throws InvalidParameterException {
        astaService.updatePresentFields(updatedAstaDiVenditoreDto, existingAstaDiVenditore);

        // Non è possibile modificare l'associazione "proprietario" tramite la risorsa "aste/di-venditori"
    }
}
