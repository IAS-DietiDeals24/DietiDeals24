package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiVenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.OffertaDiVenditoreService;
import com.iasdietideals24.backend.services.OffertaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Service;

@Service
public class OffertaDiVenditoreServiceImpl implements OffertaDiVenditoreService {

    private final OffertaService offertaService;
    private final RelationsConverter relationsConverter;

    protected OffertaDiVenditoreServiceImpl(OffertaService offertaService, RelationsConverter relationsConverter) {
        this.offertaService = offertaService;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(OffertaDiVenditoreDto offertaDiVenditoreDto) throws InvalidParameterException {
        offertaService.checkFieldsValid(offertaDiVenditoreDto);
        checkVenditoreCollegatoValid(offertaDiVenditoreDto.getVenditoreCollegatoShallow());
    }

    private void checkVenditoreCollegatoValid(AccountShallowDto venditoreCollegatoShallow) throws InvalidParameterException {
        if (venditoreCollegatoShallow == null)
            throw new InvalidParameterException("Il venditore collegato non può essere null!");
    }

    @Override
    public void convertRelations(OffertaDiVenditoreDto offertaDiVenditoreDto, OffertaDiVenditore offertaDiVenditore) throws InvalidParameterException {
        offertaService.convertRelations(offertaDiVenditoreDto, offertaDiVenditore);
        convertVenditoreCollegatoShallow(offertaDiVenditoreDto.getVenditoreCollegatoShallow(), offertaDiVenditore);
    }

    private void convertVenditoreCollegatoShallow(AccountShallowDto venditoreCollegatoShallow, OffertaDiVenditore offertaDiVenditore) throws IdNotFoundException, InvalidTypeException {

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(venditoreCollegatoShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Venditore convertedVenditore) {
                offertaDiVenditore.setVenditoreCollegato(convertedVenditore);
                convertedVenditore.addOffertaCollegata(offertaDiVenditore);
            } else {
                throw new InvalidTypeException("Un venditore può essere collegato solo ad offerte di venditore!");
            }
        }
    }

    @Override
    public void updatePresentFields(OffertaDiVenditoreDto updatedOffertaDiVenditoreDto, OffertaDiVenditore existingOffertaDiVenditore) throws InvalidParameterException {
        offertaService.updatePresentFields(updatedOffertaDiVenditoreDto, existingOffertaDiVenditore);

        // Non è possibile modificare l'associazione "venditoreCollegato" tramite la risorsa "offerte/di-venditori"
    }
}
