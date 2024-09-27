package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.OffertaDiCompratore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiCompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.OffertaDiCompratoreService;
import com.iasdietideals24.backend.services.OffertaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Service;

@Service
public class OffertaDiCompratoreServiceImpl implements OffertaDiCompratoreService {

    private final OffertaService offertaService;
    private final RelationsConverter relationsConverter;

    protected OffertaDiCompratoreServiceImpl(OffertaService offertaService, RelationsConverter relationsConverter) {
        this.offertaService = offertaService;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(OffertaDiCompratoreDto offertaDiCompratoreDto) throws InvalidParameterException {
        offertaService.checkFieldsValid(offertaDiCompratoreDto);
        checkCompratoreCollegatoValid(offertaDiCompratoreDto.getCompratoreCollegatoShallow());
    }

    private void checkCompratoreCollegatoValid(AccountShallowDto compratoreCollegatoShallow) throws InvalidParameterException {
        if (compratoreCollegatoShallow == null)
            throw new InvalidParameterException("Il compratore collegato non può essere null!");
    }

    @Override
    public void convertRelations(OffertaDiCompratoreDto offertaDiCompratoreDto, OffertaDiCompratore offertaDiCompratore) throws InvalidParameterException {
        offertaService.convertRelations(offertaDiCompratoreDto, offertaDiCompratore);
        convertCompratoreCollegatoShallow(offertaDiCompratoreDto.getCompratoreCollegatoShallow(), offertaDiCompratore);
    }

    private void convertCompratoreCollegatoShallow(AccountShallowDto compratoreCollegatoShallow, OffertaDiCompratore offertaDiCompratore) throws IdNotFoundException, InvalidTypeException {

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(compratoreCollegatoShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Compratore convertedCompratore) {
                offertaDiCompratore.setCompratoreCollegato(convertedCompratore);
                convertedCompratore.addOffertaCollegata(offertaDiCompratore);
            } else {
                throw new InvalidTypeException("Un compratore può essere collegato solo ad offerte di compratore!");
            }
        }

    }

    @Override
    public void updatePresentFields(OffertaDiCompratoreDto updatedOffertaDiCompratoreDto, OffertaDiCompratore existingOffertaDiCompratore) throws InvalidParameterException {
        offertaService.updatePresentFields(updatedOffertaDiCompratoreDto, existingOffertaDiCompratore);

        // Non è possibile modificare l'associazione "compratoreCollegato" tramite la risorsa "offerte/di-compratori"
    }
}
