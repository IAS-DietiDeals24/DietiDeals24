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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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

        log.debug("Verifico l'integrità dei dati di offerta di compratore...");

        offertaService.checkFieldsValid(offertaDiCompratoreDto);
        checkCompratoreCollegatoValid(offertaDiCompratoreDto.getCompratoreCollegatoShallow());

        log.debug("Integrità dei dati di offerta di compratore verificata.");
    }

    private void checkCompratoreCollegatoValid(AccountShallowDto compratoreCollegatoShallow) throws InvalidParameterException {

        log.trace("Controllo che 'compratoreCollegato' sia valido...");

        if (compratoreCollegatoShallow == null)
            throw new InvalidParameterException("Il compratore collegato non può essere null!");

        log.trace("'compratoreCollegato' valido.");
    }

    @Override
    public void convertRelations(OffertaDiCompratoreDto offertaDiCompratoreDto, OffertaDiCompratore offertaDiCompratore) throws InvalidParameterException {

        log.debug("Recupero le associazioni di offerta di compratore...");

        offertaService.convertRelations(offertaDiCompratoreDto, offertaDiCompratore);
        convertCompratoreCollegatoShallow(offertaDiCompratoreDto.getCompratoreCollegatoShallow(), offertaDiCompratore);

        log.debug("Associazioni di offerta di compratore recuperate.");
    }

    private void convertCompratoreCollegatoShallow(AccountShallowDto compratoreCollegatoShallow, OffertaDiCompratore offertaDiCompratore) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'compratoreCollegato'...");

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(compratoreCollegatoShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Compratore convertedCompratore) {
                offertaDiCompratore.setCompratoreCollegato(convertedCompratore);
                convertedCompratore.addOffertaCollegata(offertaDiCompratore);
            } else {
                throw new InvalidTypeException("Un compratore può essere collegato solo ad offerte di compratore!");
            }
        }

        log.trace("'compratoreCollegato' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(OffertaDiCompratoreDto updatedOffertaDiCompratoreDto, OffertaDiCompratore existingOffertaDiCompratore) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di offerta di compratore richieste...");

        offertaService.updatePresentFields(updatedOffertaDiCompratoreDto, existingOffertaDiCompratore);

        log.debug("Modifiche di offerta di compratore effettuate correttamente.");

        // Non è possibile modificare l'associazione "compratoreCollegato" tramite la risorsa "offerte/di-compratori"
    }
}
