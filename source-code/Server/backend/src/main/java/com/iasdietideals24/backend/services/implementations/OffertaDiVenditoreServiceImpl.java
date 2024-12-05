package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDiVenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.OffertaDiVenditoreService;
import com.iasdietideals24.backend.services.OffertaService;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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

        log.debug("Verifico l'integrità dei dati di offerta di venditore...");

        offertaService.checkFieldsValid(offertaDiVenditoreDto);
        checkVenditoreCollegatoValid(offertaDiVenditoreDto.getVenditoreCollegatoShallow());

        log.debug("Integrità dei dati di offerta di venditore verificata.");
    }

    private void checkVenditoreCollegatoValid(AccountShallowDto venditoreCollegatoShallow) throws InvalidParameterException {

        log.trace("Controllo che 'venditoreCollegato' sia valido...");

        if (venditoreCollegatoShallow == null)
            throw new InvalidParameterException("Il venditore collegato non può essere null!");

        log.trace("'venditoreCollegato' valido.");
    }

    @Override
    public void convertRelations(OffertaDiVenditoreDto offertaDiVenditoreDto, OffertaDiVenditore offertaDiVenditore) throws InvalidParameterException {

        log.debug("Recupero le associazioni di offerta di venditore...");

        offertaService.convertRelations(offertaDiVenditoreDto, offertaDiVenditore);
        convertVenditoreCollegatoShallow(offertaDiVenditoreDto.getVenditoreCollegatoShallow(), offertaDiVenditore);

        log.debug("Associazioni di offerta di venditore recuperate.");
    }

    private void convertVenditoreCollegatoShallow(AccountShallowDto venditoreCollegatoShallow, OffertaDiVenditore offertaDiVenditore) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'venditoreCollegato'...");

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(venditoreCollegatoShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Venditore convertedVenditore) {
                offertaDiVenditore.setVenditoreCollegato(convertedVenditore);
                convertedVenditore.addOffertaCollegata(offertaDiVenditore);
            } else {
                throw new InvalidTypeException("Un venditore può essere collegato solo ad offerte di venditore!");
            }
        }

        log.trace("'venditoreCollegato' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(OffertaDiVenditoreDto updatedOffertaDiVenditoreDto, OffertaDiVenditore existingOffertaDiVenditore) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di offerta di venditore richieste...");

        offertaService.updatePresentFields(updatedOffertaDiVenditoreDto, existingOffertaDiVenditore);

        log.debug("Modifiche di offerta di venditore effettuate correttamente.");

        // Non è possibile modificare l'associazione "venditoreCollegato" tramite la risorsa "offerte/di-venditori"
    }
}